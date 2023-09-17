package com.mohsen.nobka

import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.mohsen.nobka.core.utils.Either
import com.mohsen.nobka.data.repository.NobkaRepositoryImpl
import com.mohsen.nobka.domain.model.NobkaMoviesModel
import com.mohsen.nobka.platform.CoroutineTestExtension
import com.mohsen.nobka.ui.nobka.NobkaViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@SmallTest
@ExtendWith(CoroutineTestExtension::class)
class NobkaViewModelTest {

    private lateinit var nobkaViewModel: NobkaViewModel

    @MockK
    private lateinit var repository: NobkaRepositoryImpl


    private var movieList = mutableListOf<NobkaMoviesModel>()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        nobkaViewModel = NobkaViewModel(repository)
    }

    private fun fakeMovieModelsFactory() {
        movieList.add(NobkaMoviesModel())
    }

    @Test
    fun `when fetchDataRequest call should nobkaUiState be Success`() = runTest {
        fakeMovieModelsFactory()
        coEvery { repository.getMovies() } returns flowOf(Either.Right(movieList))
        nobkaViewModel.fetchDataRequest()

        verify(exactly = 1) { repository.getMovies() }
        nobkaViewModel.nobkaUiState.test {
            assert(awaitItem() is NobkaViewModel.NobkaUiState.Success)
        }
    }

}
