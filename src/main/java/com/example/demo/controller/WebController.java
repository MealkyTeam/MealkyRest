package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Meal;
import com.example.demo.model.MealIngredient;
import com.example.demo.model.Unit;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.MealRepository;
import com.example.demo.repository.UnitRepository;
import com.example.demo.repository.UserRepository;

@RestController
public class WebController {
@Autowired
CategoryRepository crepo;
@Autowired
IngredientRepository irepo;
@Autowired
MealRepository mrepo;
@Autowired
UnitRepository urepo;
@Autowired
UserRepository usrepo;

@GetMapping("/save")
String save()
{
	String s="";
	try {
	Category zupy = new Category("zupy");
	Category ob = new Category("obiad");
	Ingredient i1 = new Ingredient("dynia");
	Ingredient i2 = new Ingredient("maslo");
	Ingredient i3 = new Ingredient("pomarancze");
	User us1 = new User("admin", "1234", "1@1.com");
	User us2 = new User("admin1", "1234", "1@1.com");
	User us3 = new User("admin2", "1234", "1@1.com");
	Unit u1 = new Unit("kilogram");
	Unit u2 = new Unit("gram");
	Meal m1 = new Meal();
	Meal m2 = new Meal();
	Meal m3 = new Meal();
	m1.setName("Pumpkin puree");
	m1.setPrep_time(10);
	m1.setPreparation("To steam the pumpkin, peel and seed it, then cut into evenly sized cubes. Put the cubes in a steamer or colander set over a pan of simmering water and cook for 10 mins. Test with the point of a knife and cook for a further 5 mins if not cooked through. Mash and leave to cool. Alternatively, to microwave the pumpkin, cut it in half (no need to peel it or cut out the seeds) and sit cut-side up in the microwave. Cook for 20 mins, then check the flesh is soft by poking it with a fork. Keep cooking if you d like it softer. Scoop the flesh into a bowl, then mash and leave to cool. ");
	m1.setImages(new String[] {"https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2018/10/pumpkin-puree-main.jpg;"});
	
	m2.setName("Spiced lemon & ginger biscuits");
	m2.setPrep_time(20);
	m2.setPreparation("Mix the butter and sugar with a wooden spoon. Stir in the spices, lemon zest and flour, then tip in the candied lemon peel and stem ginger – you might need to get your hands in to bring the mix together as a dough. Divide the dough in two and shape each half into a log about 5cm across. Wrap in cling film, then chill for 1 hr. You can freeze the unbaked dough for up to three months. Heat oven to 180C/160C fan/gas 4. Slice the logs into 1cm-thick rounds, place on two baking trays lined with baking parchment and bake for 12-15 mins. Leave to cool completely on the tray. Mix the lemon juice with the icing sugar to make a thin glaze. Brush over the biscuits and leave to set. Will keep for three days in an airtight container.");
	m2.setImages(new String[] {"https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2018/10/shortbread.jpg;"});
	
	m3.setName("Easy-peasy fruitcake");
	m3.setPrep_time(30);
	m3.setPreparation("Put the rum (or brandy), orange zest and juice and mixed dried fruit in a bowl and stir. Leave to soak overnight. Heat oven to 170C/150C fan/gas 3½. Double line a 20cm tin with baking parchment. Beat the butter and sugar together until light and fluffy. Whisk in the eggs one by one, then fold in the almonds and flour. Add a pinch of salt and fold in the soaked fruit mixture (and any remaining liquid in the bowl), along with the nuts, candied peel and ginger. Spoon the mixture into the tin and level the surface. Bake for 1 hr, then turn the oven down to 150C/130C fan/gas 2 and bake for a further 2 hrs. Check the cake to see if it's pulling away from the sides of the tin and feels firm on top. If you need to, keep cooking for a further 15 mins. Cool in the tin. If storing in the tin, wrap the cake tightly first. Will freeze for up to two months. To decorate, brush the cake with the apricot jam (or glaze) and arrange your choice of candied fruit on top. Will keep in a sealed container for up to three weeks. ");
	m3.setImages(new String[] {"https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2018/10/easy-fruit-cake.jpg;"});
	MealIngredient mi = new MealIngredient();
	mi.setMeal(m1);
	mi.setIngredient(i1);
	mi.setQuantity(3);
	m1.getMealigredient().add(mi);
	mi.setUnit(u1);
	MealIngredient mi2 = new MealIngredient();
	mi2.setMeal(m2);
	mi2.setIngredient(i3);
	mi2.setQuantity(4);
	mi2.setUnit(u2);
	m2.getMealigredient().add(mi2);
	MealIngredient mi3 = new MealIngredient();
	mi3.setMeal(m3);
	mi3.setIngredient(i3);
	mi3.setQuantity(4);
	mi3.setUnit(u2);
	MealIngredient mi4 = new MealIngredient();
	mi4.setMeal(m3);
	mi4.setIngredient(i2);
	mi4.setQuantity(10);
	mi4.setUnit(u1);
	m3.getMealigredient().add(mi3);
	m3.getMealigredient().add(mi4);
	
	zupy.getMeals().add(m1);
	zupy.getMeals().add(m2);
	ob.getMeals().add(m1);
	ob.getMeals().add(m3);
	m1.getFavourite().add(us1);
	m1.getFavourite().add(us2);
	m1.getFavourite().add(us3);
	m2.getFavourite().add(us2);
	m1.setAuthor(us1);
	m2.setAuthor(us3);
	m3.setAuthor(us2);
	irepo.save(i1);
	irepo.save(i2);
	irepo.save(i3);
	urepo.save(u1);
	urepo.save(u2);
	crepo.save(zupy);
	crepo.save(ob);
	s="okay";
	}catch(Exception e)
	{
		s=e.getMessage();
	}
	return s;
}
}
