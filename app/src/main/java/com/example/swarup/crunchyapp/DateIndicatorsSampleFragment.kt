package com.example.swarup.crunchyapp

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_sample.*
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import ru.cleverpumpkin.calendar.utils.getColorInt
import java.util.*


class DateIndicatorsSampleFragment : Fragment() {

    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = view.findViewById(R.id.calendar_view)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.run {
            setTitle(R.string.date_indicators_sample)
            setNavigationIcon(R.drawable.ic_arrow_back_24dp)
            setNavigationOnClickListener { activity?.onBackPressed() }

            inflateMenu(R.menu.menu_today_action)
            setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
                calendarView.moveToDate(CalendarDate.today)
                return@OnMenuItemClickListener true
            })
        }

        calendarView.datesIndicators = generateCalendarDateIndicators()

        calendarView.onDateClickListener = { date ->
            val dateIndicators = calendarView.getDateIndicators(date)
                .filterIsInstance<CalendarDateIndicator>()
                .toTypedArray()

            if (dateIndicators.isNotEmpty()) {
                val builder = AlertDialog.Builder(context!!)
                    .setTitle("$date")
                    .setAdapter(DateIndicatorsDialogAdapter(context!!, dateIndicators), null)

                val dialog = builder.create()
                dialog.show()
            }
        }



        calendarView.onDateLongClickListener = { date ->

            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Choose")
                .setItems(R.array.choices) { dialog, which ->
                    if (which == 0) {
                        Toast.makeText(context, "nth", Toast.LENGTH_SHORT).show()
                    } else if (which == 1) {
                        //Toast.makeText(context, "second one", Toast.LENGTH_SHORT).show()
                        val mcurrentTime = Calendar.getInstance()
                        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                        val minute = mcurrentTime.get(Calendar.MINUTE)
                        val mTimePicker: TimePickerDialog
                        mTimePicker = TimePickerDialog(
                            context,
                            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                                tv_time_picker.text = selectedHour.toString() + ":" + selectedMinute
                            },
                            hour,
                            minute,
                            true
                        )
                        mTimePicker.setTitle("Select Time")
                        mTimePicker.show()
                    }

                }

            val choiceDialog = builder.create()

            choiceDialog.show()

        }

        if (savedInstanceState == null) {
            calendarView.setupCalendar(selectionMode = CalendarView.SelectionMode.NON)
        }
    }

    private fun generateCalendarDateIndicators(): List<CalendarView.DateIndicator> {
        val context = context!!
        val calendar = Calendar.getInstance()

        val indicators = mutableListOf<CalendarView.DateIndicator>()

        repeat(10) {
            indicators += CalendarDateIndicator(
                eventName = "Event #1",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_1_color),
                eventTime = "10:30 - 11:45"
            )

            indicators += CalendarDateIndicator(
                eventName = "Event #2",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_2_color),
                eventTime = "15:00 - 16:45"
            )

            indicators += CalendarDateIndicator(
                eventName = "Event #3",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_3_color),
                eventTime = "1:30 - 2:45"
            )

            indicators += CalendarDateIndicator(
                eventName = "Event #4",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_4_color),
                eventTime = "16:30 - 16:45"
            )

            indicators += CalendarDateIndicator(
                eventName = "Event #5",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.event_5_color),
                eventTime = "17:30 - 18:45"
            )

            calendar.add(Calendar.DAY_OF_MONTH, 5)
        }

        return indicators
    }

    class CalendarDateIndicator(
        override val date: CalendarDate,
        override val color: Int,
        val eventName: String,
        val eventTime: String

    ) : CalendarView.DateIndicator

    class DateIndicatorsDialogAdapter(
        context: Context,
        events: Array<CalendarDateIndicator>
    ) : ArrayAdapter<CalendarDateIndicator>(context, 0, events) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = if (convertView == null) {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_dialog_date_indicator, parent, false)
            } else {
                convertView
            }

            val event = getItem(position)
            view.findViewById<View>(R.id.color_view).setBackgroundColor(event.color)
            view.findViewById<TextView>(R.id.event_name_view).text = event.eventName
            view.findViewById<TextView>(R.id.event_time_view).text = event.eventTime

            view.setOnClickListener {
                Toast.makeText(context, event.eventName, Toast.LENGTH_SHORT).show()
            }

            return view
        }
    }
}