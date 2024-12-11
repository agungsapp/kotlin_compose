package com.example.tbptb.data

data class ProjectData(
    val id: String? = null,
    var nama_project: String? = null,
    var deskripsi: String? = null,
    var `object`: String? = null,
    var is_finish: Boolean? = null,
    var created_at: String? = null,
    var collaborators: List<Collaborator>? = null
)

data class Collaborator(
    val id: String,
    val email: String,
    val nama: String,
    val is_owner: Boolean
)