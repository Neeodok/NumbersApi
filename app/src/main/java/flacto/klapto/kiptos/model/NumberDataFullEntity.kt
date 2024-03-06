package flacto.klapto.kiptos.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "numbers",
    /*
      indices = [Index(value = ["text"], unique = true)]
  */
    indices = [
        Index(value = ["number"], unique = true),
        Index(value = ["textTrivia"]),
        Index(value = ["textMath"]),
        Index(value = ["textDate"])
    ]
)


data class NumberDataFullEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    val found: Boolean,

    val number: Int,

    val textTrivia: String,
    val textMath: String,
    val textDate: String,

    )

data class NumberDataRaw(
    @SerializedName("found")
    val found: Boolean,

    @SerializedName("number")
    val number: Int,

    @SerializedName("text")
    val text: String,

    @SerializedName("type")
    val type: String
)