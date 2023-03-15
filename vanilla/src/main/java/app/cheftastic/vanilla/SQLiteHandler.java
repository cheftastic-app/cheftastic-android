package app.cheftastic.vanilla;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;

import app.cheftastic.vanilla.model.Ingredient;
import app.cheftastic.vanilla.model.IngredientAmount;
import app.cheftastic.vanilla.model.NutritionalGroup;
import app.cheftastic.vanilla.model.Recipe;
import app.cheftastic.vanilla.model.RecipeCategory;
import app.cheftastic.vanilla.model.RecipeStep;
import app.cheftastic.vanilla.model.MenuRecipe;
import app.cheftastic.vanilla.model.RecipeType;
import app.cheftastic.vanilla.model.ProductType;
import app.cheftastic.vanilla.model.UnitOfMeasurement;
import app.cheftastic.vanilla.model.UnitOfTime;

public class SQLiteHandler extends SQLiteOpenHelper {

    public static final String TABLE_METADATA = "Metadatos";
    public static final String TABLE_MENU_RECIPE = "PlatoMenu";
    public static final String TABLE_RECIPE = "Receta";
    public static final String TABLE_RECIPE_COLUMN_ID = "IdReceta";
    public static final String TABLE_RECIPE_COLUMN_USER_SCORE = "PuntuacionUsuario";
    public static final String TABLE_RECIPE_COLUMN_VEGETARIAN = "Vegetariano";
    public static final String TABLE_RECIPE_COLUMN_GLUTEN_FREE = "SinGluten";
    public static final String TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1 = "CantidadGrupoNutricional1";
    public static final String TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2 = "CantidadGrupoNutricional2";
    public static final String TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3 = "CantidadGrupoNutricional3";
    public static final String TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4 = "CantidadGrupoNutricional4";
    public static final String TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5 = "CantidadGrupoNutricional5";
    public static final String TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6 = "CantidadGrupoNutricional6";
    public static final String TABLE_RECIPE_COLUMN_SCORED = "CalculosRealizados";
    public static final String TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID = "IdDiaUltimoUso";
    public static final String TABLE_RECIPE_COLUMN_LAS_USAGE_DAY_ID_TEMP = "IdDiaUltimoUsoTemp";
    public static final String TABLE_MENU_RECIPE_COLUMN_DAY_ID = "IdDia";
    public static final String TABLE_MENU_RECIPE_COLUMN_MEAL_ID = "IdComida";
    public static final String TABLE_MENU_RECIPE_COLUMN_RECIPE_CATEGORY_ID = "IdCategoriaPlato";
    public static final String TABLE_MENU_RECIPE_COLUMN_RECIPE_ID = "IdReceta";
    public static final String TABLE_METADATA_COLUMN_KEY = "Clave";
    public static final String TABLE_METADATA_COLUMN_INTEGER_VALUE = "ValorEntero";
    public static final String SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID = "CategoriaPlato_IdCategoriaPlato";
    public static final String SELECT_TABLE_RECIPE_CATEGORY_COLUMN_NAME = "CategoriaPlato_Nombre";
    public static final String SELECT_TABLE_RECIPE_TYPE_COLUMN_RECIPE_TYPE_ID = "TipoPlato_IdTipoPlato";
    public static final String SELECT_TABLE_RECIPE_TYPE_COLUMN_NAME = "TipoPlato_Nombre";
    public static final String SELECT_TABLE_RECIPE_COLUMN_ID = "Receta_IdReceta";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NAME = "Receta_Nombre";
    public static final String SELECT_TABLE_RECIPE_COLUMN_DESCRIPTION = "Receta_Descripcion";
    public static final String SELECT_TABLE_RECIPE_COLUMN_COOK_TIME = "Receta_TiempoPreparacion";
    public static final String SELECT_TABLE_RECIPE_COLUMN_DIFFICULTY = "Receta_Dificultad";
    public static final String SELECT_TABLE_RECIPE_COLUMN_SERVE_HOT = "Receta_ServirCaliente";
    public static final String SELECT_TABLE_RECIPE_COLUMN_TOTAL_SCORE = "Receta_PuntuacionTotal";
    public static final String SELECT_TABLE_RECIPE_COLUMN_VOTE_COUNT = "Receta_NumeroVotos";
    public static final String SELECT_TABLE_RECIPE_COLUMN_USER_SCORE = "Receta_PuntuacionUsuario";
    public static final String SELECT_TABLE_RECIPE_COLUMN_VERIFIED = "Receta_Revisado";
    public static final String SELECT_TABLE_RECIPE_COLUMN_USER_FREQUENT = "Receta_HabitualUsuario";
    public static final String SELECT_TABLE_RECIPE_COLUMN_CREATION_DATE = "Receta_FechaAlta";
    public static final String SELECT_TABLE_RECIPE_COLUMN_VEGETARIAN = "Receta_Vegetariano";
    public static final String SELECT_TABLE_RECIPE_COLUMN_GLUTEN_FREE = "Receta_SinGluten";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NOT_YET_COMPUTED = "Receta_SinCalcular";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1 = "Receta_CantidadGrupoNutricional1";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2 = "Receta_CantidadGrupoNutricional2";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3 = "Receta_CantidadGrupoNutricional3";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4 = "Receta_CantidadGrupoNutricional4";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5 = "Receta_CantidadGrupoNutricional5";
    public static final String SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6 = "Receta_CantidadGrupoNutricional6";
    public static final String SELECT_TABLE_RECIPE_COLUMN_COMPUTED = "Receta_CalculosRealizados";
    public static final String SELECT_TABLE_RECIPE_COLUMN_USER_ID = "Receta_IdUsuario";
    public static final String SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID = "Receta_IdDiaUltimoUso";
    public static final String SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP = "Receta_IdDiaUltimoUsoTemp";
    public static final String SELECT_TABLE_UNIT_OF_TIME_COLUMN_ID = "UnidadTiempo_IdUnidadTiempo";
    public static final String SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME = "UnidadTiempo_Nombre";
    public static final String SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME_PLURAL = "UnidadTiempo_NombrePlural";
    public static final String SELECT_TABLE_UNIT_OF_TIME_COLUMN_SYMBOL = "UnidadTiempo_Simbolo";
    public static final String SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_ID = "UnidadMedida_IdUnidadMedida";
    public static final String SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME = "UnidadMedida_Nombre";
    public static final String SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL = "UnidadMedida_NombrePlural";
    public static final String SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL = "UnidadMedida_Simbolo";
    public static final String SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_CONVERSION_FACTOR = "UnidadMedida_FactorConversion";
    public static final String SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_ID = "UnidadMedidaReferencia_IdUnidadMedida";
    public static final String SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME = "UnidadMedidaReferencia_Nombre";
    public static final String SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL = "UnidadMedidaReferencia_NombrePlural";
    public static final String SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL = "UnidadMedidaReferencia_Simbolo";
    public static final String SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_CONVERSION_FACTOR = "UnidadMedidaReferencia_FactorConversion";
    public static final String SELECT_TABLE_RECIPE_STEPS_COLUMN_ID = "PasosReceta_IdPasosReceta";
    public static final String SELECT_TABLE_RECIPE_STEPS_COLUMN_RECIPE_ID = "PasosReceta_IdReceta";
    public static final String SELECT_TABLE_RECIPE_STEPS_COLUMN_SORTING = "PasosReceta_Orden";
    public static final String SELECT_TABLE_RECIPE_STEPS_COLUMN_DESCRIPTION = "PasosReceta_Descripcion";
    public static final String SELECT_TABLE_RECIPE_STEPS_COLUMN_DURATION = "PasosReceta_Tiempo";
    public static final String SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_ID = "GrupoNutricional_IdGrupoNutricional";
    public static final String SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_NAME = "GrupoNutricional_Nombre";
    public static final String SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_DETAIL = "GrupoNutricional_Detalle";
    public static final String SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_RECOMENDED_PERCENTAGE = "GrupoNutricional_PorcentajeRecomendado";
    public static final String SELECT_TABLE_PRODUCT_TYPE_COLUMN_ID = "TipoProducto_IdTipoProducto";
    public static final String SELECT_TABLE_PRODUCT_TYPE_COLUMN_NAME = "TipoProducto_Nombre";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_ID = "Ingrediente_IdIngrediente";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_NAME = "Ingrediente_Nombre";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_PLURAL_NAME = "Ingrediente_NombrePlural";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_UNITARY_WEIGHT = "Ingrediente_PesoUnidad";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_CONTAINS_GLUTEN = "Ingrediente_ContieneGluten";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_VEGETARIAN = "Ingrediente_Vegetariano";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_VERIFIED = "Ingrediente_Revisado";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_CREATION_DATE = "Ingrediente_FechaAlta";
    public static final String SELECT_TABLE_INGREDIENT_COLUMN_USER_ID = "Ingrediente_IdUsuario";
    public static final String SELECT_TABLE_RECIPE_INGREDIENT_COLUMN_AMOUNT_PER_PERSON = "IngredientesReceta_CantidadPersona";
    public static final String SELECT_TABLE_MENU_RECIPE_COLUMN_ID = "PlatoMenu_IdDia";
    public static final String SELECT_TABLE_MENU_RECIPE_COLUMN_MEAL_ID = "PlatoMenu_IdComida";
    public static final String SELECT_TABLE_MENU_RECIPE_COLUMN_RECIPE_CATEGORY_ID = "PlatoMenu_IdCategoriaPlato";
    public static final String SELECT_TABLE_MENU_RECIPE_COLUMN_RECIPE_ID = "PlatoMenu_IdReceta";
    public static final String TABLE_METADATA_COLUMN_DB_VERSION = "VersionDB";
    public static final String TABLE_METADATA_COLUMN_LAST_UPDATED_VALUE = "UltimoDatoActualizado";
    public static final String QUERY_GET_DAILY_MENU = "SELECT PlatoMenu.IdDia \"PlatoMenu_IdDia\", PlatoMenu.IdComida \"PlatoMenu_IdComida\", PlatoMenu.IdCategoriaPlato \"PlatoMenu_IdCategoriaPlato\", PlatoMenu.IdReceta \"PlatoMenu_IdReceta\" FROM PlatoMenu WHERE PlatoMenu.IdDia = ?;";
    public static final String QUERY_GET_NUTRITIONAL_GROUP_BY_ID = "SELECT GrupoNutricional.IdGrupoNutricional \"GrupoNutricional_IdGrupoNutricional\", GrupoNutricional.Nombre \"GrupoNutricional_Nombre\", GrupoNutricional.Detalle \"GrupoNutricional_Detalle\", GrupoNutricional.PorcentajeRecomendado \"GrupoNutricional_PorcentajeRecomendado\" FROM GrupoNutricional WHERE GrupoNutricional.IdGrupoNutricional = ?;";
    public static final String QUERY_GET_INGREDIENTS_BY_RECIPE = "SELECT IngredientesReceta.IdReceta \"IngredientesReceta_IdReceta\", Ingrediente.IdIngrediente \"Ingrediente_IdIngrediente\", Ingrediente.Nombre \"Ingrediente_Nombre\", Ingrediente.NombrePlural \"Ingrediente_NombrePlural\", Ingrediente.PesoUnidad \"Ingrediente_PesoUnidad\", Ingrediente.ContieneGluten \"Ingrediente_ContieneGluten\", Ingrediente.Vegetariano \"Ingrediente_Vegetariano\", Ingrediente.Revisado \"Ingrediente_Revisado\", Ingrediente.FechaAlta \"Ingrediente_FechaAlta\", Ingrediente.IdUsuario \"Ingrediente_IdUsuario\", TipoProducto.IdTipoProducto \"TipoProducto_IdTipoProducto\", TipoProducto.Nombre \"TipoProducto_Nombre\", GrupoNutricional.IdGrupoNutricional \"GrupoNutricional_IdGrupoNutricional\", GrupoNutricional.Nombre \"GrupoNutricional_Nombre\", GrupoNutricional.Detalle \"GrupoNutricional_Detalle\", GrupoNutricional.PorcentajeRecomendado \"GrupoNutricional_PorcentajeRecomendado\", UnidadMedida.IdUnidadMedida \"UnidadMedida_IdUnidadMedida\", UnidadMedida.Nombre \"UnidadMedida_Nombre\", UnidadMedida.NombrePlural \"UnidadMedida_NombrePlural\", UnidadMedida.Simbolo \"UnidadMedida_Simbolo\", UnidadMedida.FactorConversion \"UnidadMedida_FactorConversion\", UnidadMedidaReferencia.IdUnidadMedida \"UnidadMedidaReferencia_IdUnidadMedida\", UnidadMedidaReferencia.Nombre \"UnidadMedidaReferencia_Nombre\", UnidadMedidaReferencia.NombrePlural \"UnidadMedidaReferencia_NombrePlural\", UnidadMedidaReferencia.Simbolo \"UnidadMedidaReferencia_Simbolo\", UnidadMedidaReferencia.IdUnidadReferencia \"UnidadMedidaReferencia_IdUnidadReferencia\", UnidadMedidaReferencia.FactorConversion \"UnidadMedidaReferencia_FactorConversion\", IngredientesReceta.CantidadPersona \"IngredientesReceta_CantidadPersona\" FROM IngredientesReceta CROSS JOIN Ingrediente ON IngredientesReceta.IdIngrediente = Ingrediente.IdIngrediente LEFT OUTER JOIN TipoProducto ON Ingrediente.IdTipoProducto = TipoProducto.IdTipoProducto LEFT OUTER JOIN GrupoNutricional ON TipoProducto.IdGrupoNutricional = GrupoNutricional.IdGrupoNutricional LEFT OUTER JOIN UnidadMedida ON IngredientesReceta.IdUnidadMedida = UnidadMedida.IdUnidadMedida LEFT OUTER JOIN UnidadMedida UnidadMedidaReferencia ON UnidadMedida.IdUnidadReferencia = UnidadMedidaReferencia.IdUnidadMedida WHERE IngredientesReceta.IdReceta = ?;";
    public static final String QUERY_GET_STEPS_BY_RECIPE = "SELECT PasosReceta.IdPasosReceta \"PasosReceta_IdPasosReceta\", PasosReceta.IdReceta \"PasosReceta_IdReceta\", PasosReceta.Orden \"PasosReceta_Orden\", PasosReceta.Descripcion \"PasosReceta_Descripcion\", UnidadTiempo.IdUnidadTiempo \"UnidadTiempo_IdUnidadTiempo\", UnidadTiempo.Nombre \"UnidadTiempo_Nombre\", UnidadTiempo.NombrePlural \"UnidadTiempo_NombrePlural\", UnidadTiempo.Simbolo \"UnidadTiempo_Simbolo\", PasosReceta.Tiempo \"PasosReceta_Tiempo\" FROM PasosReceta LEFT OUTER JOIN UnidadTiempo ON PasosReceta.IdUnidadTiempo = UnidadTiempo.IdUnidadTiempo WHERE PasosReceta.IdReceta = ? ORDER BY PasosReceta.Orden ASC;";
    public static final String QUERY_GET_RECIPES_FOR_ALGORITHM_BY_CATEGORY = "SELECT Receta.IdReceta \"Receta_IdReceta\", Receta.PuntuacionUsuario \"Receta_PuntuacionUsuario\", Receta.CantidadGrupoNutricional1 \"Receta_CantidadGrupoNutricional1\", Receta.CantidadGrupoNutricional2 \"Receta_CantidadGrupoNutricional2\", Receta.CantidadGrupoNutricional3 \"Receta_CantidadGrupoNutricional3\", Receta.CantidadGrupoNutricional4 \"Receta_CantidadGrupoNutricional4\", Receta.CantidadGrupoNutricional5 \"Receta_CantidadGrupoNutricional5\", Receta.CantidadGrupoNutricional6 \"Receta_CantidadGrupoNutricional6\", Receta.IdDiaUltimoUso \"Receta_IdDiaUltimoUso\", Receta.IdDiaUltimoUsoTemp \"Receta_IdDiaUltimoUsoTemp\", TipoPlato.IdTipoPlato \"TipoPlato_IdTipoPlato\", CategoriaPlato.IdCategoriaPlato \"CategoriaPlato_IdCategoriaPlato\" FROM Receta, TipoPlato, CategoriaPlato WHERE Receta.IdTipoPlato = TipoPlato.IdTipoPlato AND TipoPlato.IdCategoriaPlato = CategoriaPlato.IdCategoriaPlato AND CategoriaPlato.IdCategoriaPlato = ?;";
    public static final String QUERY_GET_LAST_RECIPE_USAGE_DATE_BY_ID = "SELECT Receta.IdReceta \"Receta_IdReceta\", Receta.IdDiaUltimoUso \"Receta_IdDiaUltimoUso\", Receta.IdDiaUltimoUsoTemp \"Receta_IdDiaUltimoUsoTemp\" FROM Receta WHERE Receta.IdReceta = ?;";
    public static final String QUERY_GET_RECIPES_WITHOUT_SCORING = "SELECT Receta.IdReceta \"Receta_IdReceta\", Receta.Nombre \"Receta_Nombre\", Receta.Descripcion \"Receta_Descripcion\", Receta.TiempoPreparacion \"Receta_TiempoPreparacion\", Receta.Dificultad \"Receta_Dificultad\", Receta.ServirCaliente \"Receta_ServirCaliente\", Receta.PuntuacionTotal \"Receta_PuntuacionTotal\", Receta.NumeroVotos \"Receta_NumeroVotos\", Receta.PuntuacionUsuario \"Receta_PuntuacionUsuario\", Receta.Revisado \"Receta_Revisado\", Receta.HabitualUsuario \"Receta_HabitualUsuario\", Receta.FechaAlta \"Receta_FechaAlta\", Receta.Vegetariano \"Receta_Vegetariano\", Receta.SinGluten \"Receta_SinGluten\", Receta.CantidadGrupoNutricional1 \"Receta_CantidadGrupoNutricional1\", Receta.CantidadGrupoNutricional2 \"Receta_CantidadGrupoNutricional2\", Receta.CantidadGrupoNutricional3 \"Receta_CantidadGrupoNutricional3\", Receta.CantidadGrupoNutricional4 \"Receta_CantidadGrupoNutricional4\", Receta.CantidadGrupoNutricional5 \"Receta_CantidadGrupoNutricional5\", Receta.CantidadGrupoNutricional6 \"Receta_CantidadGrupoNutricional6\", Receta.CalculosRealizados \"Receta_CalculosRealizados\", Receta.IdDiaUltimoUso \"Receta_IdDiaUltimoUso\", Receta.IdDiaUltimoUsoTemp \"Receta_IdDiaUltimoUsoTemp\", Receta.IdUsuario \"Receta_IdUsuario\", TipoPlato.IdTipoPlato \"TipoPlato_IdTipoPlato\", TipoPlato.Nombre \"TipoPlato_Nombre\", CategoriaPlato.IdCategoriaPlato \"CategoriaPlato_IdCategoriaPlato\", CategoriaPlato.Nombre \"CategoriaPlato_Nombre\" FROM Receta, TipoPlato, CategoriaPlato WHERE Receta.IdTipoPlato = TipoPlato.IdTipoPlato AND TipoPlato.IdCategoriaPlato = CategoriaPlato.IdCategoriaPlato AND Receta.IdReceta IN (SELECT Receta.IdReceta FROM Receta WHERE CalculosRealizados = 0);";
    public static final String QUERY_GET_RECIPES_WITHOUT_SCORING_COUNT = "SELECT COUNT(*) \"Receta_SinCalcular\" FROM Receta WHERE CalculosRealizados = 0;";
    public static final String QUERY_GET_RECIPE_BY_ID = "SELECT Receta.IdReceta \"Receta_IdReceta\", Receta.Nombre \"Receta_Nombre\", Receta.Descripcion \"Receta_Descripcion\", Receta.TiempoPreparacion \"Receta_TiempoPreparacion\", Receta.Dificultad \"Receta_Dificultad\", Receta.ServirCaliente \"Receta_ServirCaliente\", Receta.PuntuacionTotal \"Receta_PuntuacionTotal\", Receta.NumeroVotos \"Receta_NumeroVotos\", Receta.PuntuacionUsuario \"Receta_PuntuacionUsuario\", Receta.Revisado \"Receta_Revisado\", Receta.HabitualUsuario \"Receta_HabitualUsuario\", Receta.FechaAlta \"Receta_FechaAlta\", Receta.Vegetariano \"Receta_Vegetariano\", Receta.SinGluten \"Receta_SinGluten\", Receta.CantidadGrupoNutricional1 \"Receta_CantidadGrupoNutricional1\", Receta.CantidadGrupoNutricional2 \"Receta_CantidadGrupoNutricional2\", Receta.CantidadGrupoNutricional3 \"Receta_CantidadGrupoNutricional3\", Receta.CantidadGrupoNutricional4 \"Receta_CantidadGrupoNutricional4\", Receta.CantidadGrupoNutricional5 \"Receta_CantidadGrupoNutricional5\", Receta.CantidadGrupoNutricional6 \"Receta_CantidadGrupoNutricional6\", Receta.CalculosRealizados \"Receta_CalculosRealizados\", Receta.IdDiaUltimoUso \"Receta_IdDiaUltimoUso\", Receta.IdDiaUltimoUsoTemp \"Receta_IdDiaUltimoUsoTemp\", Receta.IdUsuario \"Receta_IdUsuario\", TipoPlato.IdTipoPlato \"TipoPlato_IdTipoPlato\", TipoPlato.Nombre \"TipoPlato_Nombre\", CategoriaPlato.IdCategoriaPlato \"CategoriaPlato_IdCategoriaPlato\", CategoriaPlato.Nombre \"CategoriaPlato_Nombre\" FROM Receta, TipoPlato, CategoriaPlato WHERE Receta.IdTipoPlato = TipoPlato.IdTipoPlato AND TipoPlato.IdCategoriaPlato = CategoriaPlato.IdCategoriaPlato AND Receta.IdReceta = ?;";
    public static final String QUERY_GET_RECIPES_BY_CATEGORY = "SELECT Receta.IdReceta \"Receta_IdReceta\", Receta.Nombre \"Receta_Nombre\", Receta.Descripcion \"Receta_Descripcion\", Receta.Revisado \"Receta_Revisado\", Receta.Vegetariano \"Receta_Vegetariano\", Receta.SinGluten \"Receta_SinGluten\", TipoPlato.IdTipoPlato \"TipoPlato_IdTipoPlato\", CategoriaPlato.IdCategoriaPlato \"CategoriaPlato_IdCategoriaPlato\" FROM Receta, TipoPlato, CategoriaPlato WHERE Receta.IdTipoPlato = TipoPlato.IdTipoPlato AND TipoPlato.IdCategoriaPlato = CategoriaPlato.IdCategoriaPlato AND CategoriaPlato.IdCategoriaPlato = ? ORDER BY Receta.Nombre ASC;";
    public static final String QUERY_GET_RECIPE_CATEGORIES_WITH_RECIPES = "SELECT DISTINCT TipoPlato.IdCategoriaPlato \"CategoriaPlato_IdCategoriaPlato\" FROM Receta, TipoPlato WHERE Receta.IdTipoPlato = TipoPlato.IdTipoPlato ORDER BY TipoPlato.IdTipoPlato ASC;";
    public static final String QUERY_GET_RECIPE_CATEGORIES_BY_ID = "SELECT CategoriaPlato.IdCategoriaPlato \"CategoriaPlato_IdCategoriaPlato\", CategoriaPlato.Nombre \"CategoriaPlato_Nombre\" FROM CategoriaPlato WHERE CategoriaPlato.IdCategoriaPlato = ?;";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "cheftastic-vanilla.db";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static int getLastUpdatedData() {
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();
        Resources r = App.getContext().getResources();

        if (db != null) {
            Cursor c = db.query(TABLE_METADATA, new String[]{TABLE_METADATA_COLUMN_INTEGER_VALUE}, TABLE_METADATA_COLUMN_KEY + " = ?", new String[]{TABLE_METADATA_COLUMN_LAST_UPDATED_VALUE}, null, null, null, null);
            c.moveToFirst();

            int result = c.getInt(c.getColumnIndex(TABLE_METADATA_COLUMN_INTEGER_VALUE));
            c.close();
            db.close();
            return result;
        }

        return 0;
    }

    private static UnitOfMeasurement retrieveUnitOfMeasurement(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME)
                && attributes.contains(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL)
                && attributes.contains(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL)
                && attributes.contains(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_CONVERSION_FACTOR)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME));
            String namePlural = c.getString(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL));
            String symbol = c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL)) ? null : c.getString(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL));
            Double conversionFactor = c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_CONVERSION_FACTOR)) ? null : c.getDouble(c.getColumnIndex(SELECT_TABLE_UNIT_OF_MEASUREMENT_COLUMN_CONVERSION_FACTOR));
            UnitOfMeasurement referenceUnit = retrieveReferenceUnitOfMeasurement(c);

            return new UnitOfMeasurement(id, name, namePlural, symbol, referenceUnit, conversionFactor);
        }

        return null;
    }

    private static UnitOfMeasurement retrieveReferenceUnitOfMeasurement(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME)
                && attributes.contains(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL)
                && attributes.contains(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL)
                && attributes.contains(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_CONVERSION_FACTOR)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME));
            String namePlural = c.getString(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_NAME_PLURAL));
            String symbol = c.isNull(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL)) ? null : c.getString(c.getColumnIndex(SELECT_TABLE_REFERENCE_UNIT_OF_MEASUREMENT_COLUMN_SYMBOL));

            return new UnitOfMeasurement(id, name, namePlural, symbol, null, null);
        }

        return null;
    }

    private static UnitOfTime retrieveUnitOfTime(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_UNIT_OF_TIME_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME)
                && attributes.contains(SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME_PLURAL)
                && attributes.contains(SELECT_TABLE_UNIT_OF_TIME_COLUMN_SYMBOL)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME_PLURAL))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME));
            String namePlural = c.getString(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_NAME_PLURAL));
            String symbol = c.isNull(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_SYMBOL)) ? null : c.getString(c.getColumnIndex(SELECT_TABLE_UNIT_OF_TIME_COLUMN_SYMBOL));

            return new UnitOfTime(id, name, namePlural, symbol);
        }

        return null;
    }

    public static NutritionalGroup retrieveNutritionalGroup(int id) {
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_NUTRITIONAL_GROUP_BY_ID, new String[]{Integer.toString(id)});
            c.moveToFirst();
            NutritionalGroup nutritionalGroup = retrieveNutritionalGroup(c);

            c.close();
            db.close();
            return nutritionalGroup;
        }

        return null;
    }

    private static NutritionalGroup retrieveNutritionalGroup(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_NAME)
                && attributes.contains(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_DETAIL)
                && attributes.contains(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_RECOMENDED_PERCENTAGE)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_DETAIL))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_RECOMENDED_PERCENTAGE))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_NAME));
            String details = c.getString(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_DETAIL));
            Double percentage = c.getDouble(c.getColumnIndex(SELECT_TABLE_NUTRITIONAL_GROUP_COLUMN_RECOMENDED_PERCENTAGE));

            return new NutritionalGroup(id, name, details, percentage);
        }

        return null;
    }

    private static ProductType retrieveProductType(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_PRODUCT_TYPE_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_PRODUCT_TYPE_COLUMN_NAME)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_PRODUCT_TYPE_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_PRODUCT_TYPE_COLUMN_NAME))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_PRODUCT_TYPE_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_PRODUCT_TYPE_COLUMN_NAME));
            NutritionalGroup nutritionalGroup = retrieveNutritionalGroup(c);

            return new ProductType(id, name, nutritionalGroup);
        }

        return null;
    }

    private static Ingredient retrieveIngredient(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_NAME)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_PLURAL_NAME)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_UNITARY_WEIGHT)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_CONTAINS_GLUTEN)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_VEGETARIAN)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_VERIFIED)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_CREATION_DATE)
                && attributes.contains(SELECT_TABLE_INGREDIENT_COLUMN_USER_ID)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_PLURAL_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_VERIFIED))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_CREATION_DATE))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_USER_ID))
                ) {

            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_NAME));
            String namePlural = c.getString(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_PLURAL_NAME));
            Integer unitaryWeight = c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_UNITARY_WEIGHT)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_UNITARY_WEIGHT));
            Boolean containsGluten = c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_CONTAINS_GLUTEN)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_CONTAINS_GLUTEN)) != 0;
            Boolean isVegetarian = c.isNull(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_VEGETARIAN)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_VEGETARIAN)) != 0;
            Boolean isVerified = c.getInt(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_VERIFIED)) != 0;
            String creationDate = c.getString(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_CREATION_DATE));
            ProductType productType = retrieveProductType(c);
            int userId = c.getInt(c.getColumnIndex(SELECT_TABLE_INGREDIENT_COLUMN_USER_ID));

            if (productType != null) {
                return new Ingredient(id, name, namePlural, unitaryWeight, containsGluten, isVegetarian, isVerified, creationDate, productType, userId);
            }

            return null;
        }

        return null;
    }

    private static IngredientAmount retrieveIngredientAmount(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_RECIPE_INGREDIENT_COLUMN_AMOUNT_PER_PERSON)
                ) {
            Ingredient ingredient = retrieveIngredient(c);
            UnitOfMeasurement unitOfMeasurement = retrieveUnitOfMeasurement(c);
            Double amountPerPerson = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_INGREDIENT_COLUMN_AMOUNT_PER_PERSON)) ? null : c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_INGREDIENT_COLUMN_AMOUNT_PER_PERSON));

            if (ingredient != null) {
                if (unitOfMeasurement == null || amountPerPerson == null) {
                    return new IngredientAmount(ingredient, null, null);
                }

                return new IngredientAmount(ingredient, unitOfMeasurement, amountPerPerson);
            }
        }

        return null;
    }

    private static RecipeStep retrieveRecipeStep(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_RECIPE_STEPS_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_STEPS_COLUMN_RECIPE_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_STEPS_COLUMN_SORTING)
                && attributes.contains(SELECT_TABLE_RECIPE_STEPS_COLUMN_DESCRIPTION)
                && attributes.contains(SELECT_TABLE_RECIPE_STEPS_COLUMN_DURATION)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_RECIPE_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_SORTING))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_DESCRIPTION))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_ID));
            int recipeId = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_RECIPE_ID));
            int sorting = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_SORTING));
            String description = c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_DESCRIPTION));
            Integer duration = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_DURATION)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_STEPS_COLUMN_DURATION));
            UnitOfTime unitOfTime = retrieveUnitOfTime(c);

            return new RecipeStep(id, recipeId, sorting, description, unitOfTime, duration);
        }

        return null;
    }

    public static RecipeCategory retrieveRecipeCategory(int id) {
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_RECIPE_CATEGORIES_BY_ID, new String[]{Integer.toString(id)});
            c.moveToFirst();
            RecipeCategory recipeCategory = retrieveRecipeCategory(c);

            c.close();
            db.close();
            return recipeCategory;
        }

        return null;
    }

    public static int[] getCategoryIdsWithRecipes() {
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_RECIPE_CATEGORIES_WITH_RECIPES, null);
            if (!c.moveToFirst()) {
                return null;
            }

            ArrayList<Integer> categoryIds = new ArrayList<Integer>();
            do {
                ArrayList<String> attributes = new ArrayList<String>();
                Collections.addAll(attributes, c.getColumnNames());
                if (attributes.contains(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID)
                        && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID))) {
                    categoryIds.add(c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID)));
                }
            } while (c.moveToNext());


            c.close();
            db.close();

            int[] ids = new int[categoryIds.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = categoryIds.get(i);
            }

            return ids;
        }

        return null;
    }

    private static RecipeCategory retrieveRecipeCategory(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_NAME)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_NAME))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_NAME));

            return new RecipeCategory(id, name);
        }

        return null;
    }

    private static RecipeType retrieveRecipeType(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_RECIPE_TYPE_COLUMN_RECIPE_TYPE_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_TYPE_COLUMN_NAME)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_TYPE_COLUMN_RECIPE_TYPE_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_TYPE_COLUMN_NAME))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_TYPE_COLUMN_RECIPE_TYPE_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_TYPE_COLUMN_NAME));
            RecipeCategory recipeCategory = retrieveRecipeCategory(c);

            if (recipeCategory != null) {
                return new RecipeType(id, name, recipeCategory);
            }
        }

        return null;
    }

    public static Recipe retrieveRecipe(int id) {
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_RECIPE_BY_ID, new String[]{Integer.toString(id)});
            c.moveToFirst();
            Recipe recipe = retrieveRecipe(c);

            if (recipe != null) {
                recipe.setIngredients(retrieveRecipeIngredients(db.rawQuery(QUERY_GET_INGREDIENTS_BY_RECIPE, new String[]{Integer.toString(id)})));
                recipe.setSteps(retrieveRecipeSteps(db.rawQuery(QUERY_GET_STEPS_BY_RECIPE, new String[]{Integer.toString(id)})));
            }

            c.close();
            db.close();
            return recipe;
        }

        return null;
    }

    public static ArrayList<Recipe> retrieveListOfRecipes(int categoryId) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_RECIPES_BY_CATEGORY, new String[]{Integer.toString(categoryId)});
            while (c.moveToNext()) {
                Recipe recipe = retrieveRecipeForList(c);

                if (recipe != null) {
                    recipes.add(recipe);
                }
            }

            c.close();
            db.close();
            return recipes;
        }

        return null;
    }

    public static ArrayList<Recipe> retrieveRecipesToCompute(int categoryId) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_RECIPES_FOR_ALGORITHM_BY_CATEGORY, new String[]{String.valueOf(categoryId)});
            while (c.moveToNext()) {
                Recipe recipe = retrieveRecipeToCompute(c);

                if (recipe != null) {
                    recipes.add(recipe);
                }
            }

            c.close();
            db.close();
            return recipes;
        }

        return null;
    }

    private static Recipe retrieveRecipe(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NAME)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_DESCRIPTION)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_COOK_TIME)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_DIFFICULTY)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_SERVE_HOT)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_TOTAL_SCORE)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_VOTE_COUNT)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_USER_SCORE)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_VERIFIED)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_USER_FREQUENT)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_CREATION_DATE)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_VEGETARIAN)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_GLUTEN_FREE)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_COMPUTED)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_USER_ID)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_TOTAL_SCORE))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VOTE_COUNT))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VERIFIED))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_CREATION_DATE))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_COMPUTED))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_ID))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NAME));
            String description = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_DESCRIPTION)) ? null : c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_DESCRIPTION));
            Integer cookTime = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_COOK_TIME)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_COOK_TIME));
            Integer difficulty = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_DIFFICULTY)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_DIFFICULTY));
            Boolean serveHot = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_SERVE_HOT)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_SERVE_HOT)) != 0;
            Double totalScore = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_TOTAL_SCORE));
            int voteCount = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VOTE_COUNT));
            Double userScore = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_SCORE)) ? null : c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_SCORE));
            Boolean isVerified = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VERIFIED)) != 0;
            Boolean isUserFrequent = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_FREQUENT)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_FREQUENT)) != 0;
            String creationDate = c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_CREATION_DATE));
            Boolean isVegetarian = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VEGETARIAN)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VEGETARIAN)) != 0;
            Boolean isGlutenFree = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_GLUTEN_FREE)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_GLUTEN_FREE)) != 0;
            Double[] nutritionalGroupsAmounts = new Double[6];
            nutritionalGroupsAmounts[0] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1));
            nutritionalGroupsAmounts[1] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2));
            nutritionalGroupsAmounts[2] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3));
            nutritionalGroupsAmounts[3] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4));
            nutritionalGroupsAmounts[4] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5));
            nutritionalGroupsAmounts[5] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6));
            Boolean isComputed = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_COMPUTED)) != 0;
            Long lastUsageDayId = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID)) ? null : c.getLong(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID));
            Long lastUsageDayIdTemp = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP)) ? null : c.getLong(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP));
            RecipeType recipeType = retrieveRecipeType(c);
            int userId = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_ID));

            if (recipeType != null) {
                return new Recipe(id, name, description, cookTime, difficulty, serveHot, totalScore, voteCount, userScore, isVerified, isUserFrequent, creationDate, isVegetarian, isGlutenFree, nutritionalGroupsAmounts[0], nutritionalGroupsAmounts[1], nutritionalGroupsAmounts[2], nutritionalGroupsAmounts[3], nutritionalGroupsAmounts[4], nutritionalGroupsAmounts[5], isComputed, lastUsageDayId, lastUsageDayIdTemp, recipeType, userId);
            }
        }

        return null;
    }

    private static Recipe retrieveRecipeForList(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NAME)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_DESCRIPTION)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_VERIFIED)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_VEGETARIAN)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_GLUTEN_FREE)
                && attributes.contains(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NAME))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VERIFIED))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_ID));
            String name = c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NAME));
            String description = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_DESCRIPTION)) ? null : c.getString(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_DESCRIPTION));
            Boolean isVerified = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VERIFIED)) != 0;
            Boolean isVegetarian = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VEGETARIAN)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_VEGETARIAN)) != 0;
            Boolean isGlutenFree = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_GLUTEN_FREE)) ? null : c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_GLUTEN_FREE)) != 0;
            int categoryId = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID));

            return new Recipe(id, name, description, null, null, null, null, 0, null, isVerified, null, "", isVegetarian, isGlutenFree, null, null, null, null, null, null, null, null, null, new RecipeType(0, "", new RecipeCategory(categoryId, "")), 0);
        }

        return null;
    }

    private static Recipe retrieveRecipeToCompute(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_USER_SCORE)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP)
                && attributes.contains(SELECT_TABLE_RECIPE_TYPE_COLUMN_RECIPE_TYPE_ID)
                && attributes.contains(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_TYPE_COLUMN_RECIPE_TYPE_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID))
                ) {
            int id = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_ID));
            Double userScore = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_SCORE)) ? null : c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_USER_SCORE));
            Double[] nutritionalGroupsAmounts = new Double[6];
            nutritionalGroupsAmounts[0] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1));
            nutritionalGroupsAmounts[1] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2));
            nutritionalGroupsAmounts[2] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3));
            nutritionalGroupsAmounts[3] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4));
            nutritionalGroupsAmounts[4] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5));
            nutritionalGroupsAmounts[5] = c.getDouble(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6));
            Long lastUsageDayId = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID)) ? null : c.getLong(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID));
            Long lastUsageDayIdTemp = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP)) ? null : c.getLong(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP));
            int recipeTypeId = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_TYPE_COLUMN_RECIPE_TYPE_ID));
            int categoryId = c.getInt(c.getColumnIndex(SELECT_TABLE_RECIPE_CATEGORY_COLUMN_CATEGORY_ID));

            return new Recipe(id, null, null, null, null, null, null, 0, userScore, null, null, "", null, null, nutritionalGroupsAmounts[0], nutritionalGroupsAmounts[1], nutritionalGroupsAmounts[2], nutritionalGroupsAmounts[3], nutritionalGroupsAmounts[4], nutritionalGroupsAmounts[5], null, lastUsageDayId, lastUsageDayIdTemp, new RecipeType(recipeTypeId, "", new RecipeCategory(categoryId, "")), 0);
        }

        return null;
    }

    public static void updateRecipeDayLastUsage(int recipeId, Long lastUsageDayId) {
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_LAST_RECIPE_USAGE_DATE_BY_ID, new String[]{Integer.toString(recipeId)});
            ArrayList<String> attributes = new ArrayList<String>();
            Collections.addAll(attributes, c.getColumnNames());

            if (c.moveToFirst()
                    && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID)
                    && attributes.contains(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP)) {
                Long storedDayId = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID)) ? null : c.getLong(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID));
                Long storedDayIdTemp = c.isNull(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP)) ? null : c.getLong(c.getColumnIndex(SELECT_TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID_TEMP));

                if (lastUsageDayId == null) {
                    lastUsageDayId = storedDayIdTemp;
                    storedDayIdTemp = null;
                } else {
                    storedDayIdTemp = storedDayId;
                }

                ContentValues values = new ContentValues();
                values.put(TABLE_RECIPE_COLUMN_LAST_USAGE_DAY_ID, lastUsageDayId);
                values.put(TABLE_RECIPE_COLUMN_LAS_USAGE_DAY_ID_TEMP, storedDayIdTemp);
                db.update(TABLE_RECIPE, values, TABLE_RECIPE_COLUMN_ID + " = ?", new String[]{Integer.toString(recipeId)});
            }

            c.close();
            db.close();
        }
    }

    private static TreeMap<Integer, RecipeStep> retrieveRecipeSteps(Cursor c) {
        c.moveToFirst();

        TreeMap<Integer, RecipeStep> recipeSteps = new TreeMap<Integer, RecipeStep>();

        while (!c.isBeforeFirst() && !c.isAfterLast()) {
            RecipeStep p = retrieveRecipeStep(c);
            if (p != null) {
                recipeSteps.put(p.getSorting(), p);
            }
            c.moveToNext();
        }

        c.close();
        return recipeSteps;
    }

    private static ArrayList<IngredientAmount> retrieveRecipeIngredients(Cursor c) {
        c.moveToFirst();

        ArrayList<IngredientAmount> ingredients = new ArrayList<IngredientAmount>();

        while (!c.isBeforeFirst() && !c.isAfterLast()) {
            IngredientAmount i = retrieveIngredientAmount(c);
            if (i != null) {
                ingredients.add(i);
            }

            c.moveToNext();
        }

        c.close();
        return ingredients;
    }

    public static boolean updateUserScore(float score, int recipeId) {
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getWritableDatabase();
        Resources r = App.getContext().getResources();

        if (db != null && r != null) {
            String table = TABLE_RECIPE;
            String columnRecipeId = TABLE_RECIPE_COLUMN_ID;
            String columnUserScore = TABLE_RECIPE_COLUMN_USER_SCORE;
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(columnUserScore, score);

            db.update(table, updatedValues, columnRecipeId + "=?", new String[]{String.valueOf(recipeId)});
            db.close();

            return true;
        }

        return false;
    }

    public static Collection<MenuRecipe> retrieveDailyMenu(long dayId) {
        ArrayList<MenuRecipe> menu;
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getReadableDatabase();
        if (!CheftasticCalendar.isValidDayId(dayId)) {
            throw new NumberFormatException("El identificador del día no es un identificador válido");
        }
        menu = new ArrayList<MenuRecipe>();

        if (db != null && r != null) {
            Cursor c = db.rawQuery(QUERY_GET_DAILY_MENU, new String[]{Long.toString(dayId)});
            while (c.moveToNext()) {
                MenuRecipe recipe = retrieveMenuRecipe(c);

                if (recipe != null) {
                    menu.add(recipe);
                }
            }

            c.close();
            db.close();
            return menu;
        }

        return null;
    }

    private static MenuRecipe retrieveMenuRecipe(Cursor c) {
        Resources r = App.getContext().getResources();

        ArrayList<String> attributes = new ArrayList<String>();
        Collections.addAll(attributes, c.getColumnNames());
        if (r != null && !c.isBeforeFirst() && !c.isAfterLast()
                && attributes.contains(SELECT_TABLE_MENU_RECIPE_COLUMN_ID)
                && attributes.contains(SELECT_TABLE_MENU_RECIPE_COLUMN_MEAL_ID)
                && attributes.contains(SELECT_TABLE_MENU_RECIPE_COLUMN_RECIPE_CATEGORY_ID)
                && attributes.contains(SELECT_TABLE_MENU_RECIPE_COLUMN_RECIPE_ID)
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_MENU_RECIPE_COLUMN_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_MENU_RECIPE_COLUMN_MEAL_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_MENU_RECIPE_COLUMN_RECIPE_CATEGORY_ID))
                && !c.isNull(c.getColumnIndex(SELECT_TABLE_MENU_RECIPE_COLUMN_RECIPE_ID))
                ) {

            long dayId = c.getLong(c.getColumnIndex(SELECT_TABLE_MENU_RECIPE_COLUMN_ID));
            int mealId = c.getInt(c.getColumnIndex(SELECT_TABLE_MENU_RECIPE_COLUMN_MEAL_ID));
            int recipeId = c.getInt(c.getColumnIndex(SELECT_TABLE_MENU_RECIPE_COLUMN_RECIPE_ID));

            Recipe recipe = retrieveRecipe(recipeId);

            return new MenuRecipe(dayId, mealId, recipe);
        }

        return null;
    }

    public static boolean storeMenuRecipe(MenuRecipe p) {
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getWritableDatabase();

        if (db != null && r != null) {
            deleteMenuRecipe(p.getDayId(), p.getMealId(), p.getRecipeCategoryId());

            ContentValues values = new ContentValues();
            values.put(TABLE_MENU_RECIPE_COLUMN_DAY_ID, p.getDayId());
            values.put(TABLE_MENU_RECIPE_COLUMN_MEAL_ID, p.getMealId());
            values.put(TABLE_MENU_RECIPE_COLUMN_RECIPE_CATEGORY_ID, p.getRecipeCategoryId());
            values.put(TABLE_MENU_RECIPE_COLUMN_RECIPE_ID, p.getRecipe().getId());

            db.beginTransaction();
            db.insert(TABLE_MENU_RECIPE, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();
            return true;
        }

        return false;
    }

    public static boolean deleteMenuRecipe(long dayId, int mealId, int categoryId) {
        Resources r = App.getContext().getResources();
        SQLiteDatabase db = new SQLiteHandler(App.getContext()).getWritableDatabase();

        if (db != null && r != null) {
            String where = TABLE_MENU_RECIPE_COLUMN_DAY_ID + " = ? AND " + TABLE_MENU_RECIPE_COLUMN_MEAL_ID + " = ? AND " + TABLE_MENU_RECIPE_COLUMN_RECIPE_CATEGORY_ID + " = ?";
            String[] whereArgs = {Long.toString(dayId), Integer.toString(mealId), Integer.toString(categoryId)};
            boolean result = db.delete(TABLE_MENU_RECIPE, where, whereArgs) == 1;

            db.close();
            return result;
        }

        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS CategoriaPlato (" +
                "IdCategoriaPlato " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "" +
                "CONSTRAINT pk_CategoriaPlato " +
                "   PRIMARY KEY (IdCategoriaPlato)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS TipoPlato (" +
                "IdTipoPlato " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "IdCategoriaPlato " + "INTEGER NOT NULL, " +
                "" +
                "CONSTRAINT pk_TipoPlato " +
                "PRIMARY KEY (IdTipoPlato), " +
                "CONSTRAINT fk_TipoPlato_CategoriaPlato " +
                "FOREIGN KEY (IdCategoriaPlato) REFERENCES CategoriaPlato (IdCategoriaPlato)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS Receta (" +
                "IdReceta " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "Descripcion " + "TEXT DEFAULT NULL, " +
                "TiempoPreparacion " + "INTEGER DEFAULT NULL, " +
                "Dificultad " + "INTEGER DEFAULT NULL, " +
                "ServirCaliente " + "NUMERIC DEFAULT NULL, " +
                "PuntuacionTotal " + "REAL NOT NULL DEFAULT 0, " +
                "NumeroVotos " + "INTEGER NOT NULL DEFAULT 0, " +
                "PuntuacionUsuario " + "REAL DEFAULT NULL, " +
                "Revisado " + "NUMERIC NOT NULL DEFAULT 0, " +
                "HabitualUsuario " + "NUMERIC DEFAULT NULL, " +
                "FechaAlta " + "TEXT NOT NULL, " +
                "Vegetariano " + "NUMERIC DEFAULT NULL, " +
                "SinGluten " + "NUMERIC DEFAULT NULL, " +
                "CantidadGrupoNutricional1 " + "REAL DEFAULT 0, " +
                "CantidadGrupoNutricional2 " + "REAL DEFAULT 0, " +
                "CantidadGrupoNutricional3 " + "REAL DEFAULT 0, " +
                "CantidadGrupoNutricional4 " + "REAL DEFAULT 0, " +
                "CantidadGrupoNutricional5 " + "REAL DEFAULT 0, " +
                "CantidadGrupoNutricional6 " + "REAL DEFAULT 0, " +
                "CalculosRealizados " + "NUMERIC NOT NULL DEFAULT 0, " +
                "IdDiaUltimoUso " + "INTEGER DEFAULT NULL, " +
                "IdDiaUltimoUsoTemp " + "INTEGER DEFAULT NULL, " +
                "IdTipoPlato " + "INTEGER NOT NULL, " +
                "IdUsuario " + "INTEGER NOT NULL, " +
                "" +
                "CONSTRAINT pk_Receta " +
                "PRIMARY KEY (IdReceta), " +
                "CONSTRAINT fk_Receta_TipoPlato, " +
                "   FOREIGN KEY (IdTipoPlato) REFERENCES TipoPlato (IdTipoPlato)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS Aderezo ( " +
                "IdReceta " + "INTEGER, " +
                "IdRecetaAderezo " + "INTEGER, " +
                "" +
                "CONSTRAINT pk_Aderezo " +
                "PRIMARY KEY (IdReceta, IdRecetaAderezo), " +
                "CONSTRAINT fk_AderezoReceta_Receta " +
                "FOREIGN KEY (IdReceta) REFERENCES Receta(IdReceta), " +
                "CONSTRAINT fk_AderezoReceta_RecetaAderezo " +
                "FOREIGN KEY (IdRecetaAderezo) REFERENCES Receta (IdReceta)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS GrupoNutricional ( " +
                "IdGrupoNutricional " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "Detalle " + "TEXT NOT NULL, " +
                "PorcentajeRecomendado " + "REAL NOT NULL, " +
                "" +
                "CONSTRAINT pk_GrupoNutricional " +
                "PRIMARY KEY (IdGrupoNutricional)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS TipoProducto ( " +
                "IdTipoProducto " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "IdGrupoNutricional " + "INTEGER, " +
                "" +
                "CONSTRAINT pk_TipoProducto " +
                "PRIMARY KEY (IdTipoProducto), " +
                "CONSTRAINT fk_TipoProducto_GrupoNutricional " +
                "FOREIGN KEY (IdGrupoNutricional) REFERENCES GrupoNutricional (IdGrupoNutricional)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS Ingrediente ( " +
                "IdIngrediente " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "NombrePlural " + "TEXT UNIQUE NOT NULL, " +
                "PesoUnidad " + "INTEGER DEFAULT NULL," +
                "ContieneGluten " + "NUMERIC, " +
                "Vegetariano " + "NUMERIC, " +
                "Revisado " + "NUMERIC NOT NULL DEFAULT 0, " +
                "FechaAlta " + "TEXT NOT NULL, " +
                "IdTipoProducto " + "INTEGER NOT NULL, " +
                "IdUsuario " + "INTEGER NOT NULL, " +
                "" +
                "CONSTRAINT pk_Ingrediente " +
                "PRIMARY KEY (IdIngrediente), " +
                "CONSTRAINT fk_Ingrediente_TipoProducto " +
                "FOREIGN KEY (IdTipoProducto) REFERENCES TipoProducto (IdTipoProducto)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS UnidadTiempo ( " +
                "IdUnidadTiempo " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "NombrePlural " + "TEXT UNIQUE NOT NULL, " +
                "Simbolo " + "TEXT, " +
                "" +
                "CONSTRAINT pk_UnidadTiempo " +
                "PRIMARY KEY (IdUnidadTiempo) " +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS UnidadMedida ( " +
                "IdUnidadMedida " + "INTEGER, " +
                "Nombre " + "TEXT UNIQUE NOT NULL, " +
                "NombrePlural " + "TEXT UNIQUE NOT NULL, " +
                "Simbolo " + "TEXT, " +
                "IdUnidadReferencia " + "INTEGER, " +
                "FactorConversion " + "REAL, " +
                "" +
                "CONSTRAINT pk_UnidadMediada " +
                "PRIMARY KEY (IdUnidadMedida), " +
                "CONSTRAINT fk_UnidadMedida_UnidadReferencia " +
                "FOREIGN KEY (IdUnidadReferencia) REFERENCES UnidadMedida (IdUnidadMedida)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS IngredientesReceta (" +
                "IdReceta " + "INTEGER, " +
                "IdIngrediente " + "INTEGER, " +
                "IdUnidadMedida " + "INTEGER, " +
                "CantidadPersona " + "REAL, " +
                "" +
                "CONSTRAINT pk_IngredientesReceta " +
                "PRIMARY KEY (IdReceta, IdIngrediente), " +
                "CONSTRAINT fk_IngredientesReceta_Receta " +
                "FOREIGN KEY (IdReceta) REFERENCES Receta (IdReceta), " +
                "CONSTRAINT fk_IngredientesReceta_Ingrediente " +
                "FOREIGN KEY (IdIngrediente) REFERENCES Ingrediente (IdIngrediente), " +
                "CONSTRAINT fk_IngredientesReceta_UnidadMedida " +
                "FOREIGN KEY (IdUnidadMedida) REFERENCES UnidadMedida (IdUnidadMedida)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS PasosReceta ( " +
                "IdPasosReceta " + "INTEGER, " +
                "IdReceta " + "INTEGER NOT NULL, " +
                "Orden " + "INTEGER NOT NULL, " +
                "Descripcion " + "TEXT NOT NULL, " +
                "IdUnidadTiempo " + "INTEGER, " +
                "Tiempo " + "INTEGER, " +
                "" +
                "CONSTRAINT pk_PasosReceta " +
                "PRIMARY KEY (IdPasosReceta), " +
                "CONSTRAINT fk_PasosReceta_Receta " +
                "FOREIGN KEY (IdReceta) REFERENCES Receta (IdReceta), " +
                "CONSTRAINT fk_PasosReceta_UnidadTiempo " +
                "FOREIGN KEY (IdUnidadTiempo) REFERENCES UnidadTiempo (IdUnidadTiempo), " +
                "CONSTRAINT uindex_IdRecetaOrden " +
                "UNIQUE (IdReceta, Orden)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS PlatoMenu ( " +
                "IdDia " + "INTEGER, " +
                "IdComida " + "INTEGER, " +
                "IdCategoriaPlato " + "INTEGER, " +
                "IdReceta " + "INTEGER," +
                "" +
                "CONSTRAINT pk_PlatoMenu " +
                "PRIMARY KEY (IdDia, IdComida, IdCategoriaPlato), " +
                "CONSTRAINT fk_PlatoMenu_CategoriaPlato " +
                "FOREIGN KEY (IdCategoriaPlato) REFERENCES CategoriaPlato (IdCategoriaPlato), " +
                "CONSTRAINT fk_PlatoMenu_Receta " +
                "FOREIGN KEY (IdReceta) REFERENCES Receta (IdReceta) " +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS Metadatos ( " +
                "Clave " + "TEXT, " +
                "ValorTexto " + "TEXT DEFAULT NULL, " +
                "ValorEntero " + "INTEGER DEFAULT NULL, " +
                "ValorReal " + "REAL DEFAULT NULL, " +
                "" +
                "CONSTRAINT pk_Metadatos " +
                "PRIMARY KEY (Clave)" +
                ");");

        Resources r = App.getContext().getResources();
        ContentValues values = new ContentValues();

        values.put(TABLE_METADATA_COLUMN_KEY, TABLE_METADATA_COLUMN_DB_VERSION);
        values.put(TABLE_METADATA_COLUMN_INTEGER_VALUE, DATABASE_VERSION);
        db.insert(TABLE_METADATA, null, values);

        values.clear();
        values.put(TABLE_METADATA_COLUMN_KEY, TABLE_METADATA_COLUMN_LAST_UPDATED_VALUE);
        values.put(TABLE_METADATA_COLUMN_INTEGER_VALUE, 0);
        db.insert(TABLE_METADATA, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("CREATE TABLE IF NOT EXISTS PlatoMenu ( " +
                        "IdDia " + "INTEGER, " +
                        "IdComida " + "INTEGER, " +
                        "IdCategoriaPlato " + "INTEGER, " +
                        "IdReceta " + "INTEGER," +
                        "" +
                        "CONSTRAINT pk_PlatoMenu " +
                        "PRIMARY KEY (IdDia, IdComida, IdCategoriaPlato), " +
                        "CONSTRAINT fk_PlatoMenu_CategoriaPlato " +
                        "FOREIGN KEY (IdCategoriaPlato) REFERENCES CategoriaPlato (IdCategoriaPlato), " +
                        "CONSTRAINT fk_PlatoMenu_Receta " +
                        "FOREIGN KEY (IdReceta) REFERENCES Receta (IdReceta) " +
                        ");");

            case 2:
                db.execSQL("ALTER TABLE Receta ADD COLUMN IdDiaUltimoUso INTEGER DEFAULT NULL");
                db.execSQL("ALTER TABLE Receta ADD COLUMN IdDiaUltimoUsoTemp INTEGER DEFAULT NULL");

            case 3:
                db.execSQL("ALTER TABLE Ingrediente ADD COLUMN PesoUnidad INTEGER DEFAULT NULL");

            default:
                Resources r = App.getContext().getResources();
                ContentValues values = new ContentValues();
                values.put(TABLE_METADATA_COLUMN_INTEGER_VALUE, DATABASE_VERSION);
                db.update(TABLE_METADATA, values, TABLE_METADATA_COLUMN_KEY + " = ?", new String[]{TABLE_METADATA_COLUMN_DB_VERSION});
        }
    }
}
