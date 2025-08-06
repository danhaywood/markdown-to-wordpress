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
