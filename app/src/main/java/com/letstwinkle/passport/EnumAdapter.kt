package com.letstwinkle.passport

import android.content.Context
import android.databinding.InverseBindingMethod
import android.databinding.InverseBindingMethods
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.util.Log
import android.view.*
import android.widget.*

/**
 * NOTE: This is also taken from my personal codebase. The code is terse.
 */

@InverseBindingMethods(
    InverseBindingMethod(type = Spinner::class, attribute = "selectionEnum", method = "getSelectedItem", event = "app:selectionAttrChanged")
)
/**
 * If isOptional, then position 0 is a "No response" item.
 */
open class EnumAdapter<DispEnum>(val context: Context,
                                 enumClass: Class<DispEnum>,
                                 @LayoutRes var resource: Int = android.R.layout.simple_spinner_item,
                                 @LayoutRes var dropDownResource: Int = android.R.layout.simple_spinner_dropdown_item,
                                 val isOptional: Boolean = false,
                                 @StringRes val nullDisplay: Int = R.string.choose) :
    BaseAdapter() where DispEnum : Displayable, DispEnum : Enum<DispEnum>
{
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected val konstants: Array<DispEnum> = enumClass.enumConstants

    override fun getItem(position: Int): DispEnum? {
        if (isOptional) {
            return if (position == 0) null else konstants[position - 1]
        }
        return konstants[position]
    }

    override fun getItemId(position: Int): Long = position + 0L

    override fun getCount(): Int = konstants.size + if (isOptional) 1 else 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, resource)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, dropDownResource)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup, resource: Int): View {
        val view = convertView ?: inflater.inflate(resource, parent, false)
        val textView: TextView
        try {
            textView = view as TextView
        } catch (e: ClassCastException) {
            Log.e("EnumAdapter", "You must supply a resource ID for a TextView")
            return view
        }

        val enum = getItem(position)
        textView.text = enum?.displayString(context.resources) ?: context.getString(this.nullDisplay)
        return view
    }

    open fun optionalPosition(e: Enum<*>?): Int {
        e?.let { return it.ordinal + 1}
        return 0
    }
}

// An initial empty variant. Sacrifices optional value for a non-value default.
class InitialEmptyEnumAdapter<DispEnum>(context: Context,
                                  enumClass: Class<DispEnum>,
                                  @LayoutRes resource: Int = android.R.layout.simple_spinner_item,
                                  @LayoutRes dropDownResource: Int = android.R.layout.simple_spinner_dropdown_item)
    : EnumAdapter<DispEnum>(context, enumClass, resource, dropDownResource, true)
        where DispEnum : Displayable, DispEnum : Enum<DispEnum>
{
    override fun getItem(position: Int): DispEnum? {
        return if (position == count) null else konstants[position]
    }

    override fun optionalPosition(e: Enum<*>?): Int {
        e?.let { return it.ordinal }
        return this.count
    }

    override fun getCount(): Int = konstants.size

}