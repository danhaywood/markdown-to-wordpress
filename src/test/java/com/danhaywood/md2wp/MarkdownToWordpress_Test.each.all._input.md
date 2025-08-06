# Introducing Apache Causeway

Let's set the scene: you're about to start work on a line-of-business application within your enterprise.
It has to handle a complicated domain, and the business expect it to deliver value for them for many years to come.
How would you go about building it?

## Choosing the UI

One of the earlier decisions you'll need to make when building your app is the technology for the UI.

If you're an afficionado of front-end frameworks, you'd quite possibly reach for something like React, Angular or Vue), with corresponding controllers for the backend.
Or if you prefer Java over Javascript, and prefer your UI to be defined at the server, you might use Vaadin, PrimeFaces or even Apache Wicket.
Or you might prefer to be retro and look to HTMX, following the HATEOAS.

To speed you up, you might choose code generate your app, for example using JHipster.
Or you might well indulge in a bit of vibe coding and have an AI agent do your code generation for you; though you might end up with a bunch of code you don't necessarily understand.

## Costs and Risks

However you go about it, building the user interface typically consumes a substantial slice of the development budget; some estimates put the figure as high as 40%.
But there's a much more subtle cost to building a UI in this way, which is this: without care, that custom-written presentation layer can act as a kind of distortion force field, obscuring and obfuscating the underlying domain concepts of your application.

If you have a frontend UI team separate from the backend teams, then the risk is even higher, because of the intrinsic difficulty of explaining subtle concepts and ideas to another team who have their own priorities.
But it can happen even if there's just one team with responsibility for both domain and UI; there's always the risk that the two drift apart.

## Tackling the essential complexity of your domain

Developing the ubiquitous language of your domain requires you to work closely with the domain experts/expert users.
You might be able to use a technique such as BDD to drive out the domain concepts, but this won't work in every context; what if your domain experts aren't native speakers in your own language?

You'd be better off therefore developing the domain by showing them working software, but since all they actually see is the UI, you will - as noted above - have to work hard to make sure that the changes you make in the UI are properly reflected down into your domain classes proper.


## Naked what now?

Apache Causeway solves both the "how do we build our UI?" issue as well as the "how do we develop a good domain model?" question, and it does this by generating the UI for you.
But this isn't a compile-time generation tool (such as JHipster); instead, the UI is generated at run-time, as a projection of the underlying domain model.

It's somewhat analogous to how an ORM such as Hibernate works; Hibernate builds a metamodel at runtime and uses that to submit SQL on the fly.
Causeway does the same (it builds up a metamodel also); but it uses that metamodel to render the UI (or API).
As such, it's an implementation of the "naked objects" architecture; hopefully you can see where this (deliberately provoking) name comes from.

> [!NOTE]
By the way, a human-usable UI is only one such projection of the metamodel.
Causeway can also use it to dynamically generate a REST API for your app (along with Swagger files), as well as a complete GraphQL API.

## In the real-world

You might be sceptical that such an approach can be used to build anything other than toy/CRUD-style applications, I can understand that.
By way of counterexamples, I can tell you that the naked obejcts pattern powers the main benefit administration system for Irish Central Government, used by 7,000 officers across the country, who administer over 40 benefits (pension, child benefit etc) as well as numerous related schemes and the main identity database for the citizens of Ireland.
The app has nearly 3000 domain objects, and is 20 years old and still going strong.

For another example, Apache Causeway has been used to develop 'Estatio', an in-house ERP system for Eurocommercial Properties, who own and operate 25 shopping centres across Europe.
Estatio isn't quite as old as the Irish system, 12 years in fact, but it's in rude health.
It consists of about 300 domain concepts, is used by a about 200+ users, and processes about €500m invoices each year (though it's more than just an invoicing system).
Oh, and here's the best part: it's developed and supported by a team of just 3 developers (actually, not even; only one of us is full-time).

## Show me the money

Enough scene-setting; in the rest of this article we'll take a good look at what an Apache Causeway application looks like.
To do that, we'll start with Causeway's version of the famous petclinic app.
Causeway provides this as a tutorial app for you to learn by doing [1]; we're going to start with the final app and have a poke around at some of the implemented features.
For simplicity, I've taken a copy of the app and pushed it to a new repo on github.

First, make sure you have prerequisites:

* Java 21
* Apache Maven 3.9.9+ and/or mvndaemon (`mvnd`).
* git
* a Java IDE (IntelliJ, Eclipse, VSCode etc)

Create an empty directory, and in it clone and then build the app:

```bash
git clone https://github.com/danhaywood/petclinic-javapro .
mvn clean install -T1C      # or mvnd clean install
```

Wait for the internet to be downloaded, and then open up the app in your IDE.

### Running the app

Let's start by running the app.
Locate the `PetClinicApp`, you'll notice that this is a Spring Boot application, because Causeway as a framework sits on top of Spring Boot.
It contains a `main()` method, so you can start it immediately.

Or, if you want to run from the command line, use:

```bash
mvn spring-boot:run -pl webapp
```

If you open up http://localhost:8080, you'll see:

![Causeway welcome page](images/causeway-welcome-page.png =250x) 

Click on the "Generic UI (Wicket)", which will take you to http://localhost:8080/wicket:

![Causeway Logon Page](images/causeway-logon-page.png =200x)   

Use 'sven/pass' as the username/password (details were on the splash screen).
You should end up at the home page:

![PetClinic Home Page](images/petclinic-home-page.png)

Click on the icon for "Jamal", and you'll be viewing the page for this pet owner:

![Petowner](images/petowner-object.png)

You've already encountered your first two domain objects: the `HomePageViewModel` (a view model object), and `PetOwner` (an entity).
Both of the pages you visited were rendered dynamically and directly from the underlying domain object; there is no custom or generated controllers, templates or Javascript involved. 
So, let's now tie what you've seen back to the code.

## Domain Objects: exploring the `PetOwner` class

Locate the `PetOwner.java` class in your IDE, and navigate to its definition:

```java
@Entity
@Table(schema= PetOwnerModule.SCHEMA, 
    uniqueConstraints = {@UniqueConstraint(name = "PetOwner__name__UNQ", columnNames = {"name"})})
@EntityListeners(CausewayEntityListener.class)
@Named(PetOwnerModule.NAMESPACE + ".PetOwner")
@DomainObject
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class PetOwner implements Comparable<PetOwner> {
    ...
}
```
Many of these annotations are for JPA, but its the `@DomainObject` annotation that's significant here, identifying this as an object to be included in Causeway's metamodel.

Let's now take a closer look at the members of this class.

### Properties

Using your IDE, search for the getters of `PetOwner` class:

![PetOwner getters](images/PetOwner-getters.png =300x)

These instance fields are all rendered automatically in the UI.
Most of the fields scalars, returning strings, numbers, dates etc; most are rendered as text boxes of various hues, although `content` renders a PDF.
We call these properties.

For example, here's the `knownAs` property:

```java
@Property(editing = Editing.ENABLED)
@Column(length = 40, nullable = true, name = "knownAs")
@Getter @Setter
private String knownAs;
```

The `@Property` annotation is a Causeway annotation; you can see that it allows editing, so this value can be modified directly.
Try it in the application.

### Collections

The `pets` field on the other hand returns a collection type (`Set<Pet>`), and so it is rendered as a table; we call these collections:

```java
@Collection
@OneToMany(mappedBy = "petOwner", cascade = CascadeType.ALL, orphanRemoval = true)
@Getter
private Set<Pet> pets = new TreeSet<>();
```

> [!NOTE]
You might notice that in the UI that there is also a "visits" collection that does not appear in the `PetOwner` entity.
We'll look at this in a later article.

### Actions

Unlike the `knownAs` property, if you try to edit the `name` property you won't be able to; Causeway's default is that properties are read-only.
Instead, the application allows the name to be modified using an "action":

```java
@Action(semantics = IDEMPOTENT)
@ActionLayout(
        describedAs = "Updates the name of this object, certain characters (" + Name.PROHIBITED_CHARACTERS + ") are not allowed.")
public PetOwner updateName(
        @Name final String name) {
    setName(name);
    return this;
}
@MemberSupport public String default0UpdateName() {
    return getName();
}
```

There's quite a lot going on here, and we won't explain every feature.
But you can see that the `updateName(...)` method is annotated with Causeway's `@Action` annotation.

Another example of an action is "Add Pet":

```java
@Action
@ActionLayout(associateWith = "pets", sequence = "1")
public PetOwner addPet(@PetName final String name, final PetSpecies species) {
    pets.add(new Pet(this, name, species));
    return this;
}
@MemberSupport
public String validate0AddPet(final String name) {
    if (getPets().stream().anyMatch(x -> Objects.equals(x.getName(), name))) {
        return "This owner already has a pet called '" + name + "'";
    }
    return null;
}
```

While neither of these actions do much more than update the state of the entity (they are rather "CRUD-y"), in a larger application they could of course perform much more complicated business logic.
Even with these simple actions, though, we can see that Causeway allows validation rules to be easily written.

For example, with the "Add Pet" action, the supporting `validate0AddPet` method is used to validate the pet name (the 0th param of the `addPet` method).
If this method returns a non-`null` value, then this is displayed to the user:

  ![Validating the name of a pet](images/add-pet-validate-name.png =300x)
