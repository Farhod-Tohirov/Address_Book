package uz.star.testforanymobile.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions

/**
 * Created by Farhod Tohirov on 04-Feb-21
 **/

fun Fragment.checkPermission(permission: String, granted: () -> Unit) {
    val mContext = context ?: return
    val options = Permissions.Options()
    options.setCreateNewTask(true)
    Permissions.check(mContext, arrayOf(permission), null, options, object : PermissionHandler() {
        override fun onGranted() {
            granted()
        }
    })
}

fun Fragment.checkPermissions(permission: Array<String>, granted: () -> Unit) {
    val mContext = context ?: return
    val options = Permissions.Options()
    options.setCreateNewTask(true)
    Permissions.check(mContext, permission, null, options, object : PermissionHandler() {
        override fun onGranted() {
            granted()
        }
    })
}

fun FragmentActivity.checkPermission(permission: String, granted: () -> Unit) {
    val mContext = this
    val options = Permissions.Options()

    options.setCreateNewTask(true)
    Permissions.check(mContext, arrayOf(permission), null, options, object : PermissionHandler() {
        override fun onGranted() {
            granted()
        }
    })
}