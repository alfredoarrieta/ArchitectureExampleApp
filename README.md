# Hello and Welcome To Architecture Example App

This is a Store built using MVVM and Redux to showcase diferent architectures, it also uses:
* Navigation Component.
* Data Binding.
* Dependency Injection with Koin.

# MVVM architecture uses: 
* View Models to store information and Business Logic.
* Live Data to propagate information changes.
* Repository to interact with data.
* All fragments share the activity View Model.
* Navigation and product information is stored in ViewModel.

# Redux architecture uses:
* State to store the app information.
* Reducers to execute any change in the State.
* Epics to store all the business logic.
* RxAndroid to propagate information changes.
* All information in the AppState is App Wide available trough the AppStore.
* Views can either get the current value or subscribe to get all further changes since the state is a BehaviourSubject.

This example was created by: Alfredo Arrieta
https://www.linkedin.com/in/alfredo-josé-arrieta-bawab-85908996/
