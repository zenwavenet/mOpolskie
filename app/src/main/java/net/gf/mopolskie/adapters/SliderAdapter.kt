package net.gf.mopolskie.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.gf.mopolskie.manager.TrashManager
import net.gf.mopolskie.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SliderAdapter(
    private val trashManager: TrashManager,
    private val onTrashTileInteraction: (TrashTileAction) -> Unit
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private var currentTimeState: TimeState? = null
    private var dateTimeViewHolder: DateTimeViewHolder? = null

    private var cachedMunicipalityName: String? = null
    private var cachedEndpointId: Int? = null

    companion object {
        private const val TYPE_DATETIME = 0
        private const val TYPE_TRASH = 1
    }

    data class TimeState(val date: String, val dayOfWeek: String, val time: String)

    sealed class TrashTileAction {
        object SelectMunicipality : TrashTileAction()
        object GoToWasteActivity : TrashTileAction()
    }

    abstract class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class DateTimeViewHolder(itemView: View) : SliderViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
    }

    class TrashViewHolder(itemView: View) : SliderViewHolder(itemView) {
        val municipalityText: TextView = itemView.findViewById(R.id.selectedMunicipalityText)
        val todayTrash: TextView = itemView.findViewById(R.id.todayTrashType)
        val tomorrowTrash: TextView = itemView.findViewById(R.id.tomorrowTrashType)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_DATETIME
            1 -> TYPE_TRASH
            else -> TYPE_DATETIME
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_DATETIME -> {
                val view = inflater.inflate(R.layout.slider_tile_datetime, parent, false)
                DateTimeViewHolder(view)
            }
            TYPE_TRASH -> {
                val view = inflater.inflate(R.layout.slider_tile_trash, parent, false)
                TrashViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        when (holder) {
            is DateTimeViewHolder -> {
                dateTimeViewHolder = holder
                currentTimeState?.let { timeState ->
                    holder.dateText.text = timeState.date
                    holder.dayText.text = timeState.dayOfWeek
                    holder.timeText.text = timeState.time
                }
            }
            is TrashViewHolder -> {
                if (cachedMunicipalityName == null || cachedEndpointId == null) {
                    cachedMunicipalityName = trashManager.getSelectedMunicipalityName()
                    cachedEndpointId = trashManager.getSelectedEndpointId()
                }

                if (cachedMunicipalityName != null && cachedEndpointId != null) {
                    holder.municipalityText.text = cachedMunicipalityName

                    holder.todayTrash.text = "Ładowanie..."
                    holder.tomorrowTrash.text = "Ładowanie..."

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val (todayText, tomorrowText) = trashManager.getTodayAndTomorrowWaste()
                            
                            withContext(Dispatchers.Main) {
                                holder.todayTrash.text = todayText
                                holder.tomorrowTrash.text = tomorrowText
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            withContext(Dispatchers.Main) {
                                holder.todayTrash.text = "Błąd pobierania"
                                holder.tomorrowTrash.text = "Błąd pobierania"
                            }
                        }
                    }
                } else {
                    holder.municipalityText.text = "Wybierz gminę"
                    holder.todayTrash.text = "—"
                    holder.tomorrowTrash.text = "—"
                }

                holder.municipalityText.setOnClickListener {
                    if (cachedMunicipalityName != null && cachedEndpointId != null) {
                        onTrashTileInteraction(TrashTileAction.GoToWasteActivity)
                    } else {
                        onTrashTileInteraction(TrashTileAction.SelectMunicipality)
                    }
                }

                holder.municipalityText.setOnLongClickListener {
                    onTrashTileInteraction(TrashTileAction.SelectMunicipality)
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int = 2

    fun updateTimeState(timeState: TimeState) {
        currentTimeState = timeState

        dateTimeViewHolder?.let { holder ->
            holder.dateText.text = timeState.date
            holder.dayText.text = timeState.dayOfWeek
            holder.timeText.text = timeState.time
        }
    }
    
    override fun onViewRecycled(holder: SliderViewHolder) {
        super.onViewRecycled(holder)
        if (holder is DateTimeViewHolder) {
            dateTimeViewHolder = null
        }
    }

    fun refreshTrashData() {
        cachedMunicipalityName = trashManager.getSelectedMunicipalityName()
        cachedEndpointId = trashManager.getSelectedEndpointId()

        notifyItemChanged(0)
        notifyItemChanged(1)
    }
}
