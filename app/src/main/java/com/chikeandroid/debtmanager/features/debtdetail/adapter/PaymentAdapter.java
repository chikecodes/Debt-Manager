package com.chikeandroid.debtmanager.features.debtdetail.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.databinding.ListItemPaymentBinding;
import com.chikeandroid.debtmanager.features.debtdetail.PaymentDiffCallback;

import java.util.List;

/**
 * Created by Chike on 5/25/2017.
 * Payments RecyclerView Adapter
 */
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private final List<Payment> mPayments;
    private final LayoutInflater mLayoutInflater;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemClickListener mOnItemClickListener;

    public PaymentAdapter(Context context, List<Payment> payments) {
        mPayments = payments;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Payment payment, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, Payment payment, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ListItemPaymentBinding binding = ListItemPaymentBinding.inflate(mLayoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Payment payment = mPayments.get(position);
        holder.bind(payment);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemClick(view, payment, holder.getAdapterPosition());
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, payment, holder.getAdapterPosition());
                }
            }
        });
    }

    public void updatePaymentListItems(List<Payment> payments) {
        final PaymentDiffCallback diffCallback = new PaymentDiffCallback(this.mPayments, payments);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mPayments.clear();
        this.mPayments.addAll(payments);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mPayments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemPaymentBinding mListItemPaymentBinding;

        public ViewHolder(ListItemPaymentBinding binding) {
            super(binding.getRoot());
            mListItemPaymentBinding = binding;
        }

        public void bind(Payment payment) {
            mListItemPaymentBinding.setPayment(payment);
        }
    }
}
