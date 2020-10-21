package rocks.gioac96.veronica.samples.graphql;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.FieldCoordinates;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLTypeReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.CreationException;
import rocks.gioac96.veronica.core.providers.Singleton;

public class GraphQLProvider
    extends ConfigurableProvider<GraphQL>
    implements Singleton {

    private static final Set<Person> people = new HashSet<>();
    private static final Set<Cat> cats = new HashSet<>();

    static {

        Person giorgio = new Person("Giorgio");
        Person giorgia = new Person("Giorgia");

        Cat boezio = new Cat("Boezio", 6, giorgio);
        Cat pimpa = new Cat("Pimpa", 3, giorgio);

        giorgio.ownedCats.add(boezio);
        giorgio.ownedCats.add(pimpa);

        giorgio.friends.add(giorgia);
        giorgia.friends.add(giorgio);

        people.add(giorgio);
        people.add(giorgia);

        cats.add(boezio);
        cats.add(pimpa);

    }

    @Override
    public GraphQL instantiate() throws CreationException {

        GraphQLInterfaceType namedBeingInterfaceType = GraphQLInterfaceType.newInterface()
            .name("NamedBeing")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("name")
                .type(GraphQLString)
                .build())
            .build();


        GraphQLObjectType personType = GraphQLObjectType.newObject()
            .name("Person")
            .withInterface(namedBeingInterfaceType)
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("name")
                .type(GraphQLString)
                .build())
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("friends")
                .type(GraphQLList.list(GraphQLTypeReference.typeRef("Person")))
                .build())
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("ownedCats")
                .type(GraphQLList.list(GraphQLTypeReference.typeRef("Cat")))
                .build())
            .build();

        GraphQLObjectType catType = GraphQLObjectType.newObject()
            .name("Cat")
            .withInterface(namedBeingInterfaceType)
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("name")
                .type(GraphQLString)
                .build())
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("weight")
                .type(GraphQLInt)
                .build())
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("owner")
                .type(personType)
                .build())
            .build();

        GraphQLCodeRegistry codeRegistry = GraphQLCodeRegistry.newCodeRegistry()
            .typeResolver(
                namedBeingInterfaceType,
                env -> env.getObject() instanceof Person ? personType : catType
            )
            .dataFetcher(
                FieldCoordinates.coordinates("NamedBeing", "name"),
                (DataFetcher<String>) environment -> ((NamedBeing) environment.getSource()).name
            )
            .dataFetcher(
                FieldCoordinates.coordinates("Cat", "weight"),
                (DataFetcher<Integer>) environment -> ((Cat) environment.getSource()).weight
            )
            .dataFetcher(
                FieldCoordinates.coordinates("Cat", "owner"),
                (DataFetcher<Person>) environment -> ((Cat) environment.getSource()).owner
            )
            .dataFetcher(
                FieldCoordinates.coordinates("Person", "friends"),
                (DataFetcher<List<Person>>) environment -> new ArrayList<>(((Person) environment.getSource()).friends)
            )
            .dataFetcher(
                FieldCoordinates.coordinates("Person", "ownedCats"),
                (DataFetcher<List<Cat>>) environment -> new ArrayList<>(((Person) environment.getSource()).ownedCats)
            )
            .dataFetcher(
                FieldCoordinates.coordinates("Query", "getAllPeople"),
                (DataFetcher<List<Person>>) environment -> new ArrayList<>(people)
            )
            .build();

        GraphQLObjectType queryType = GraphQLObjectType.newObject()
            .name("Query")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("getAllPeople")
                .type(GraphQLList.list(personType))
                .build())
            .build();

        GraphQLSchema schema = GraphQLSchema.newSchema()
            .codeRegistry(codeRegistry)
            .query(queryType)
            .additionalType(namedBeingInterfaceType)
            .additionalType(personType)
            .additionalType(catType)
            .build();

        return GraphQL.newGraphQL(schema).build();

    }

    @Getter
    @AllArgsConstructor
    private static class NamedBeing {

        private final String name;

    }

    @Getter
    private static class Person extends NamedBeing {

        private final Set<Person> friends = new HashSet<>();
        private final Set<Cat> ownedCats = new HashSet<>();

        public Person(String name) {

            super(name);

        }

    }

    @Getter
    private static class Cat extends NamedBeing {

        private final int weight;

        private final Person owner;

        public Cat(String name, int weight, Person owner) {

            super(name);
            this.weight = weight;
            this.owner = owner;

        }

    }

}
