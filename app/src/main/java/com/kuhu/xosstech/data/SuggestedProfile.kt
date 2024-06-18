package com.kuhu.xosstech.data

import com.google.gson.annotations.SerializedName

data class SuggestedProfile(
    val status: String,
    val message: String,
    val data: List<UserProfile>
)

data class MatchList(
    val status: String,
    val message: String,
    @SerializedName("data") val matchData: List<MatchRequest>
)

data class ProfileViewList(
    val status: String,
    val message: String,
    @SerializedName("data") val matchData: List<ProfileView>
)

data class UserProfile(
    var isButtonClicked: Boolean = false,
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("subscription_number") val subscriptionNumber: String?,
    @SerializedName("subscription_status") val subscriptionStatus: String?,
    @SerializedName("subscription_date") val subscriptionDate: String?,
    @SerializedName("profile_picture") val profilePicture: String?, // Replace String with the actual type
    @SerializedName("images") val images: List<String>?, // Replace String with the actual type
    @SerializedName("profile_info") val profileInfo: ProfileInfo,
//    @SerializedName("contact_details") val contactDetails: ContactDetails?,
    @SerializedName("education_details") val educationDetails: EducationDetails,
//    @SerializedName("family_details") val familyDetails: FamilyDetails?,
    @SerializedName("occupation_details") val occupationDetails: OccupationDetails,
    @SerializedName("family_details") val familyInfo: FamilyInfo?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class ProfileInfo(
    @SerializedName("name") val fullName: String,
    @SerializedName("profile_summary") val profileSummary: String,
    @SerializedName("division") val division: String,
    @SerializedName("district") val district: String,
    @SerializedName("city") val city: String,
    @SerializedName("marital_status") val maritalStatus: String,
    @SerializedName("diet") val diet: String?, // Replace String with the actual type
    @SerializedName("gender") val gender: String,
    @SerializedName("religion") val religion: String,
    @SerializedName("drinking") val drinkingStatus: String,
    @SerializedName("smoking") val smokingStatus: String
)

data class FamilyInfo(
    @SerializedName("father_name") val fatherName: String,
    @SerializedName("mother_name") val motherName: String
)

/*data class ContactDetails(
    // Define properties for contact details
)*/

data class EducationDetails(
    @SerializedName("education_level") val educationLevel: String? // Replace String with the actual type
)

/*
data class FamilyDetails(
    // Define properties for family details
)
*/

data class OccupationDetails(
    @SerializedName("profession") val profession: String?,
    @SerializedName("designation") val designation: String?,
    @SerializedName("income") val income: String?,
    @SerializedName("company_name") val companyName: String?
)

data class MatchRequest(
    var isButtonClicked: Boolean = false,
    @SerializedName("id") val id: Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("matched_date") val matchedDate: String,
    @SerializedName("match_status") val matchStatus: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("sender") val sender: UserProfile
)

data class ProfileView(
    var isButtonClicked: Boolean = false,
    @SerializedName("id") val id: Int,
    @SerializedName("viewer_id") val viewerId: Int,
    @SerializedName("profile_owner_id") val profileOwnerId: Int,
    @SerializedName("viewer") val viewers: UserProfile
)
