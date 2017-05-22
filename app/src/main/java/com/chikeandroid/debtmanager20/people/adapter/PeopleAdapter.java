package com.chikeandroid.debtmanager20.people.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.data.Person;
import com.chikeandroid.debtmanager20.databinding.ListItemPersonBinding;
import com.chikeandroid.debtmanager20.people.PeopleDiffCallback;

import java.util.List;

/**
 * Created by Chike on 5/10/2017.
 * People RecyclerView Adapter
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder>  {

    private final List<Person> mPersons;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final Fragment mFragment;

    public PeopleAdapter(Fragment fragment, Context context, List<Person> persons) {
        mPersons = persons;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mFragment = fragment;
    }

    // for item click listener
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Person person, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    // for item long click listener
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemClick(View view, Person person, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemPersonBinding binding = ListItemPersonBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Person person = mPersons.get(position);
        holder.bind(person);

        holder.mListItemDebtBinding.lytParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemClick(view, person, holder.getAdapterPosition());
                }
                return true;
            }
        });

        holder.mListItemDebtBinding.lytParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, person, holder.getAdapterPosition());
                }
            }
        });
    }

    public void updatePersonListItems(List<Person> persons) {
        final PeopleDiffCallback diffCallback = new PeopleDiffCallback(this.mPersons, persons);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mPersons.clear();
        this.mPersons.addAll(persons);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemPersonBinding mListItemDebtBinding;
        String mPersonId;

        public ViewHolder(ListItemPersonBinding binding) {
            super(binding.getRoot());
            mListItemDebtBinding = binding;
        }

        public void bind(Person person) {
            mPersonId = person.getId();
            mListItemDebtBinding.setPerson(person);

            Glide.with(mFragment)
                    .load(person.getImageUri())
                    .placeholder(R.drawable.ic_avatar)
                    .dontAnimate()
                    .into(mListItemDebtBinding.image);
        }
    }

    public Person getPerson(int position) {
        return mPersons.get(position);
    }
}
