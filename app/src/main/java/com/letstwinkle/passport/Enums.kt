package com.letstwinkle.passport

import android.content.res.Resources
import android.support.annotation.StringRes

enum class SortMode(@StringRes val displayStr: Int) : Displayable {
    Default(R.string.sortDefault),
    NameAsc(R.string.sortNameAsc),
    NameDesc(R.string.sortNameDesc),
    AgeAsc(R.string.sortAgeAsc),
    AgeDesc(R.string.sortAgeDesc)
    ;

    override fun displayString(res: Resources): String = res.getString(displayStr)
}

enum class FilterMode(@StringRes val displayStr: Int) : Displayable {
    All(R.string.filterAll),
    Women(R.string.filterFemale),
    Men(R.string.filterMale)
    ;

    override fun displayString(res: Resources): String = res.getString(displayStr)
}