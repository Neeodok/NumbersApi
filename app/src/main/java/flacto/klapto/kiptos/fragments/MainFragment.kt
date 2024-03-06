package flacto.klapto.kiptos.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import flacto.klapto.kiptos.dao.MainRepository
import flacto.klapto.kiptos.databinding.FragmentMainBinding
import flacto.klapto.kiptos.model.NumberDataFullEntity
import flacto.klapto.kiptos.recyclerview.HistoryAdapter
import flacto.klapto.kiptos.utils.CheckInternetStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = b.root


    private val b by lazy { FragmentMainBinding.inflate(layoutInflater) }


    private val etNumberInput by lazy { b.etNumber }
    private val btnClear by lazy { b.btnClear }
    private val pbLoading by lazy { b.pbLoading }
    private val btnGetaFact by lazy { b.btnGetFact }
    private val btnGetRandomFact by lazy { b.btnGetRandomFact }
    private val rvHistory by lazy { b.rvHistory }

    private val historyAdapter = HistoryAdapter()


    private val repository by lazy { MainRepository(requireContext()) }

    private val checkConnection by lazy { CheckInternetStateUseCase(requireActivity().application) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbLoading.visibility = View.INVISIBLE

        setupRV()
        btnClear.setOnClickListener {
            clearDatabase()
        }
        repository.getAllNumbersDataLiveData().observe(viewLifecycleOwner) {
            historyAdapter.submitList(it)
        }




        btnGetaFact.setOnClickListener {

            val number = etNumberInput.text.toString()
            onLoadingStart()

            lifecycleScope.launch(Dispatchers.IO) {


                if (!checkConnection.isInternetAvailable()) {
                    withContext(Dispatchers.Main) {

                        onLoadingFinish()
                        Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
                    }


                    return@launch
                }
                if (number.isEmpty()){
                    withContext(Dispatchers.Main) {

                        onLoadingFinish()
                        Toast.makeText(requireContext(), "Enter a number", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                kotlin.runCatching {
                    val responses = repository.getAllNumberFacts(number)

                    val triviaResponse = responses.first
                    val mathResponse = responses.second
                    val dateResponse = responses.third

                    if (triviaResponse.isSuccessful && mathResponse.isSuccessful && dateResponse.isSuccessful) {


                        val numCompleteDataObject = NumberDataFullEntity(
                            found = true,
                            number = number.toInt(),
                            textDate = dateResponse.body()?.text ?: "",
                            textMath = mathResponse.body()?.text ?: "",
                            textTrivia = triviaResponse.body()?.text ?: ""
                        )

                        withContext(Dispatchers.Main) {
                            repository.upsert(numCompleteDataObject)
                            onLoadingFinish()
                        }

                    } else {
                        withContext(Dispatchers.Main) { onLoadingFinish() }
                    }
                }.onFailure{th ->
                    th.printStackTrace()
                    withContext(Dispatchers.Main) { onLoadingFinish() }
                }


            }

        }

        btnGetRandomFact.setOnClickListener {
            val number = Random.nextInt(1000).toString()
            onLoadingStart()

            lifecycleScope.launch(Dispatchers.IO) {


                if (!checkConnection.isInternetAvailable()) {
                    withContext(Dispatchers.Main) {

                        onLoadingFinish()
                        Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
                    }


                    return@launch
                }
                kotlin.runCatching {
                val responses = repository.getAllNumberFacts(number)

                val triviaResponse = responses.first
                val mathResponse = responses.second
                val dateResponse = responses.third

                if (triviaResponse.isSuccessful && mathResponse.isSuccessful && dateResponse.isSuccessful) {


                    val numCompleteDataObject = NumberDataFullEntity(
                        found = true,
                        number = number.toInt(),
                        textDate = dateResponse.body()?.text ?: "",
                        textMath = mathResponse.body()?.text ?: "",
                        textTrivia = triviaResponse.body()?.text ?: ""
                    )

                    withContext(Dispatchers.Main) {
                        repository.upsert(numCompleteDataObject)
                        onLoadingFinish()
                    }

                } else {

                    withContext(Dispatchers.Main) { onLoadingFinish() }

                }
                }.onFailure{th ->
                    th.printStackTrace()
                    withContext(Dispatchers.Main) { onLoadingFinish() }
                }
            }
        }


    }

    private fun setupRV() {
        rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun onLoadingStart() {
        btnGetRandomFact.isClickable = false
        btnGetaFact.isClickable = false
        pbLoading.visibility = View.VISIBLE

    }

    private fun onLoadingFinish() {
        btnGetRandomFact.isClickable = true
        btnGetaFact.isClickable = true
        pbLoading.visibility = View.INVISIBLE

    }

    private fun clearDatabase() {
        val repository = MainRepository(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            repository.clearAllData()
        }
    }
}