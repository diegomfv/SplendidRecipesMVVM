package com.diegomfv.splendidrecipesmvvm

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegomfv.domain.Recipe
import com.diegomfv.splendidrecipesmvvm.data.database.RecipeDao
import com.diegomfv.splendidrecipesmvvm.data.database.RecipesDatabase
import com.diegomfv.splendidrecipesmvvm.data.database.model.entities.CuisineDb
import com.diegomfv.splendidrecipesmvvm.data.database.model.entities.RecipeDb
import com.diegomfv.splendidrecipesmvvm.utils.TestUtil
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecipesDbTest {

    private lateinit var recipeDao: RecipeDao
    private lateinit var db: RecipesDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RecipesDatabase::class.java
        ).build()
        recipeDao = db.recipeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeRecipeAndReadList() {

        val recipe: RecipeDb = TestUtil.createRecipeDTO(0)
        recipeDao.insertRecipe(recipe)

        val recipeFromDb = recipeDao.findRecipeById(0)
        assertThat(recipeFromDb, equalTo(recipe))

    }

    @Test
    @Throws(Exception::class)
    fun ensureCuisineUniquenessTest() {

        val cuisine: CuisineDb = TestUtil.createCuisineDbDTO()
        recipeDao.insertCuisine(cuisine)
        recipeDao.insertCuisine(cuisine)

        println(recipeDao.getAllCuisines())
        assertTrue(recipeDao.getAllCuisines().size == 1)

    }

    @Test
    @Throws(Exception::class)
    fun writeRecipesAndReadAssociation() {

        val recipes: List<Recipe> = listOf(
            TestUtil.createRecipe(
                "alpha",
                listOf("Mediterranean", "Spanish"),
                listOf("lunch")),
            TestUtil.createRecipe(
                "beta",
                listOf("Mediterranean", "Italian"),
                listOf("lunch", "dinner")
            ),
            TestUtil.createRecipe(
                "charlie",
                listOf("Spanish"),
                listOf("lunch", "dinner", "breakfast")
            ),
            TestUtil.createRecipe("delta", listOf("Greek"), listOf("dinner")),
            TestUtil.createRecipe("echo", listOf(), listOf())
        )

        recipeDao.insertRecipe(recipes.component1())
        recipeDao.insertRecipe(recipes.component2())
        recipeDao.insertRecipe(recipes.component3())
        recipeDao.insertRecipe(recipes.component4())
        recipeDao.insertRecipe(recipes.component5())
        recipeDao.insertRecipe(recipes.component1())

        printDatabaseTest("///////////////////")
        recipeDao.getAllCuisines().forEach {
            printDatabaseTest(it.toString())
        }

        printDatabaseTest("///////////////////")
        recipeDao.getAllDishTypes().forEach {
            printDatabaseTest(it.toString())
        }

        printDatabaseTest("///////////////////")
        recipeDao.getAllRecipeCuisineJoin().forEach {
            printDatabaseTest(it.toString())
        }

        printDatabaseTest("///////////////////")
        recipeDao.getAllRecipeDishTypeJoin().forEach {
            printDatabaseTest(it.toString())
        }

        printDatabaseTest("///////////////////")
        recipeDao.getAllRecipeAssoc().forEach {
            printDatabaseTest(it.toString())
        }

    }

    fun printDatabaseTest(msg: String?) {
        println("DatabaseTest: $msg")
    }
}