package com.kuhu.xosstech.util

import android.accounts.AccountManager
import android.content.Context

object GoogleAccountHelper {

    fun getGoogleAccountEmails(context:Context):List<String>
    {
        val accounts = AccountManager.get(context).getAccountsByType("com.google")
        val emailLists = mutableListOf<String>()
        for(account in accounts)
        {
            if (account.name.endsWith("@gmail.com")) {
                emailLists.add(account.name)
            }
        }

        return emailLists
    }
}