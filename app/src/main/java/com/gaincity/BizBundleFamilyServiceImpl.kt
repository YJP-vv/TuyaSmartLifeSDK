package com.gaincity
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService


class BizBundleFamilyServiceImpl : AbsBizBundleFamilyService() {

    private var mHomeId: Long = -1 // Default home ID (none selected)
    private var mHomeName: String = ""

    override fun getCurrentHomeId(): Long {
        // Return the currently selected home ID
        return mHomeId
    }

    override fun shiftCurrentFamily(familyId: Long, curName: String) {
        // Update the current family details
        super.shiftCurrentFamily(familyId, curName)
        mHomeId = familyId
        mHomeName = curName
    }


}