package io.ipoli.android.quest.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.squareup.otto.Bus;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ipoli.android.R;
import io.ipoli.android.app.events.EventSource;
import io.ipoli.android.app.events.ItemActionsShownEvent;
import io.ipoli.android.app.utils.Time;
import io.ipoli.android.quest.data.SubQuest;
import io.ipoli.android.quest.events.subquests.CompleteSubQuestEvent;
import io.ipoli.android.quest.events.subquests.DeleteSubQuestEvent;
import io.ipoli.android.quest.events.subquests.UndoCompleteSubQuestEvent;
import io.ipoli.android.quest.events.subquests.UpdateSubQuestNameEvent;

/**
 * Created by Polina Zhelyazkova <polina@ipoli.io>
 * on 4/28/16.
 */
public class SubQuestListAdapter extends RecyclerView.Adapter<SubQuestListAdapter.ViewHolder> {
    protected Context context;
    protected final Bus evenBus;
    protected List<SubQuest> subQuests;

    public SubQuestListAdapter(Context context, Bus evenBus, List<SubQuest> subQuests) {
        this.context = context;
        this.evenBus = evenBus;
        this.subQuests = subQuests;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_quest_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SubQuest sq = subQuests.get(holder.getAdapterPosition());

        holder.moreMenu.setOnClickListener(v -> {
            evenBus.post(new ItemActionsShownEvent(EventSource.SUBQUESTS));
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.sub_quest_actions_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete_sub_quest:
                        removeSubquest(holder.getAdapterPosition());
                        evenBus.post(new DeleteSubQuestEvent(sq, EventSource.SUBQUESTS));
                        return true;
                }
                return false;
            });
            popupMenu.show();
        });

        holder.name.setText(sq.getName());
        holder.check.setOnCheckedChangeListener(null);
        holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sq.setCompletedAtDate(new Date());
                sq.setCompletedAtMinute(Time.now().toMinutesAfterMidnight());
                holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.name.setEnabled(false);
                evenBus.post(new CompleteSubQuestEvent(sq));
            } else {
                sq.setCompletedAtDate(null);
                sq.setCompletedAtMinute(null);
                holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.name.setEnabled(true);
                evenBus.post(new UndoCompleteSubQuestEvent(sq));
            }
        });
        holder.check.setChecked(sq.isCompleted());

        hideUnderline(holder.name);
        holder.name.setOnFocusChangeListener((view, isFocused) -> {
            if (isFocused) {
                if (sq.isCompleted()) {
                    holder.name.clearFocus();
                    return;
                }
                showUnderline(holder.name);
                holder.name.requestFocus();
            } else {
                hideUnderline(holder.name);
                sq.setName(holder.name.getText().toString());
                evenBus.post(new UpdateSubQuestNameEvent(sq, EventSource.SUBQUESTS));
            }
        });
    }

    private void removeSubquest(int position) {
        subQuests.remove(position);
        notifyItemRemoved(position);
    }

    private void showUnderline(TextInputEditText editText) {
        editText.getBackground().clearColorFilter();
    }

    private void hideUnderline(TextInputEditText editText) {
        editText.getBackground().setColorFilter(ContextCompat.getColor(context, android.R.color.transparent), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return subQuests.size();
    }

    public void addSubQuest(SubQuest subQuest) {
        subQuests.add(subQuest);
        notifyItemInserted(subQuests.size() - 1);
    }

    public void setSubQuests(List<SubQuest> subQuests) {
        this.subQuests.clear();
        this.subQuests.addAll(subQuests);
        notifyDataSetChanged();
    }

    public List<SubQuest> getSubQuests() {
        return subQuests;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sub_quest_check)
        CheckBox check;

        @BindView(R.id.sub_quest_name)
        TextInputEditText name;

        @BindView(R.id.sub_quest_more_menu)
        ImageButton moreMenu;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
