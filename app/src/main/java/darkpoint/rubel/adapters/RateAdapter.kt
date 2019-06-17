package darkpoint.rubel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import darkpoint.rubel.databinding.AllCurrenciesItemBinding
import darkpoint.rubel.model.Rate

/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


class RateAdapter( val onClickListener: OnClickListener) :
        ListAdapter<Rate, RateAdapter.RateViewHolder>(DiffCallback) {

    class RateViewHolder(private var binding: AllCurrenciesItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(rate: Rate) {
            binding.rate = rate
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Rate>() {
        override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
            return oldItem.Cur_ID == newItem.Cur_ID
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RateViewHolder {
        return RateViewHolder(AllCurrenciesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        val rate = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(rate)
        }
        holder.bind(rate)
    }


    class OnClickListener(val clickListener: (rate:Rate) -> Unit) {
        fun onClick(rate:Rate) = clickListener(rate)
    }
}
