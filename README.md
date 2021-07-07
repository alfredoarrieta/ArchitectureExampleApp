# Hello and Welcome To Architecture Example App

This is a Store built using MVVM and Redux to showcase diferent architectures, it also uses:
* Data Binding.
* Dependency Injection with Koin.
* All navigation uses transition animations.

# MVVM architecture uses: 
* View Models to store information and Business Logic.
* Live Data to propagate information changes.
* Repository to interact with data.
* All fragments share the activity View Model.
* Navigation and product information is stored in ViewModel.
* Navigation of the BottomSheet is handled by Navigation Component.

# Redux architecture uses:
* State to store the app information.
* Reducers to execute any change in the State.
* Epics to store all the business logic.
* RxAndroid to propagate information changes.
* All information in the AppState is App Wide available trough the AppStore.
* Views can either get the current value or subscribe to get all further changes since the state is a BehaviourSubject.
* Activity handles navigation and leaves fragments alive while switching.

# Screenshots

![Screenshot_1625684211](https://user-images.githubusercontent.com/85945105/124814458-a339e400-df2b-11eb-9c69-7136b159178c.png)

![Screenshot_1625684234](https://user-images.githubusercontent.com/85945105/124814585-d3818280-df2b-11eb-8832-f2f43bc14755.png)
![Screenshot_1625684236](https://user-images.githubusercontent.com/85945105/124814587-d41a1900-df2b-11eb-88db-38fae21493bc.png)
![Screenshot_1625684237](https://user-images.githubusercontent.com/85945105/124814589-d4b2af80-df2b-11eb-83e6-e872425ba899.png)

![Screenshot_1625684221](https://user-images.githubusercontent.com/85945105/124814690-f1e77e00-df2b-11eb-8a06-5a117741cb4e.png)
![Screenshot_1625684223](https://user-images.githubusercontent.com/85945105/124814692-f2801480-df2b-11eb-85b9-f936aa02e06f.png)
![Screenshot_1625684225](https://user-images.githubusercontent.com/85945105/124814694-f318ab00-df2b-11eb-820c-96a5d694ab23.png)



This example was created by Alfredo Arrieta
https://www.linkedin.com/in/alfredo-josé-arrieta-bawab-85908996/
