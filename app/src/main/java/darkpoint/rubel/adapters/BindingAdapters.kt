package darkpoint.rubel.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import darkpoint.rubel.model.Rate

@BindingAdapter("listRates")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Rate>?) {
    val adapter = recyclerView.adapter as RateAdapter
    adapter.submitList(data)
}
