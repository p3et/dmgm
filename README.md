# Directed Multigraph Miner (DMGM)
DMGM is a library for data mining in directed multigraphs. 
In a directed graph edges are directed from a source vertex to a target vertex.
In a multigraph there may be multiple edges between any pair of vertices. 
Altough popular graph models such as the [property graph model](https://github.com/tinkerpop/blueprints/wiki/Property-Graph-Model) or [RDF](https://www.w3.org/TR/rdf-concepts/) show these features, 
most existing graph mining libraries lack of their support.
DMGM is Open Source (GPL v3), open for contributions and aims to be a general framework for practical graph mining algorithms.

DMGM support the [Extended Property Graph Model](http://dbs.uni-leipzig.de/file/EPGM.pdf) and impliments a similar approach as [Gradoop](http://www.gradoop.com). In particular, both systems support pipelines of graph operators to enable the declarative expression of [complex analytical questions](http://dbs.uni-leipzig.de/file/Graph_Mining_for_Complex_Data_Analytics.pdf) about graph-structured data. However, while Gradoop is designed for a cluster of machines without shared memory, DMGM is optimized for parallel execution on a single computer.

## Database Model
All DMGM programs require a database represented by the `PropertyGraphDb` interface. However, our model supports more than a typical [property graph](https://github.com/tinkerpop/blueprints/wiki/Property-Graph-Model) and even more than the [Extended Property Graph Model](http://dbs.uni-leipzig.de/file/EPGM.pdf). Let's start with a quick walkthrough:

### Data elements
Any data element in a graph database is represented by a `long` value. Data elements are vertices, edges, graphs and graph collections.

+ *vertex (aka node or point):* The simplest data element; a simple id.
+ *edge (aka line or arc):* A connection between two vertices, i.e., a pair of two vertex ids. 
+ *graph:* A pair with of two collections of vertex ids and edge ids.
+ *graph collection:* A collection of graph ids.

Note that `long` which represents a data element may have multiple roles. For example, a data element may be vertex of a graph but represent a graph itself and edges may connect graphs with graph collections. This flexibility should be used for provenance (e.g., graph collection B was extractd from graph A) but not for algorithmic purposes.

### Labels and Properties
All data elements may have a label and arbitrary properties.

+ *label:* Any data element must have a label, i.e., a `String` that adds a semantic meaning. Examples:
  + A vertex represents a `"User"`.
  + An edges has the type `"friendship"`.
  + A graph stores a `"SocialNetwork"`.
  + A graph collections contains `"FrequentPatterns"`.
  
+ *property:* A property is a key=value pairs, for example name=Alice. Any data element may have arbitrary properties. For performance reasons every property value has a type. Currently, DMGM supports the following data types:
  + primitive: `boolean`, `long`, `double`
  + array: `int[]`. `String[]`
  + objects: `String`, `BigDecimal`, `LocalDate`
  
+ *dictionary coding:* Interally, DMGM uses dictionary coding to store labels and property keys. Thus, the API allows the use of `String` or encoded `int` values for all methods related to labels and properties. For performance reasones encoded values should be used where possible to avoid dictionary lookups.

### Implementation and API
`InMemoryGraphDb` is the reference implementation of `PropertyGraphDb`. However, the interface is designed to support arbitrary database technologies such as realtional and full-featured graph databases. Please feel free to contribute further implementations. 

```java
// CREATE DB
boolean parallelRead = true;
PropertyGraphDb db = new InMemoryGraphDb(parallelRead);

// DICTIONARY CODING
int vertexLabel = db.encode("Vertex");

// CREATE DATA ELEMENTS
long sourceVertexId = db.createVertex(vertexLabel);
long targetVertexId = db.createVertex(vertexLabel);
long edgeId = db.createEdge("Edge", sourceVertexId, targetVertexId);
long graphId = db.createGraph("Graph", new long[] {sourceVertexId, targetVertexId}, new long[] {edgeId});
long collectionId = db.createCollection("Collection", new long[] {graphId});

// READ DATA ELEMENTS
SourceIdTargetId edge = db.getSourceIdTargetId(edgeId);
// => (sourceVertexId, targetVertexId)
VertexIdsEdgeIds graph = db.getVertexIdsEdgeIds(graphId);
// => ([sourceVertexId, targetVertexId], [edgeId])
long[] collection = db.getGraphIdsOfCollection(collectionId);
// => [graphId]
long[] sourceVertexContainedInGraphIds = db.getGraphIdsOfVertex(sourceVertexId);
// => [graphId]
long[] edgeContainedInGraphIds = db.getGraphIdsOfEdge(edgeId);
// => [graphId]
    
// LABELS
int encoded = db.getLabel(edgeId);
String decoded = db.decode(encoded);
// => "Edge"
    
// PROPERTIES
int confirmedKey = db.encode("confirmedKey");
db.set(edgeId, confirmedKey, true);
boolean edgeConfirmed = db.is(edgeId, confirmedKey);
// => true
boolean graphConfirmed = db.is(graphId, confirmedKey);
// => false

db.set(graphId, "number", 1L);
long number = db.getLong(graphId, "number");
// => 1L

int numbersKey = db.encode("numbers");
db.add(graphId, numbersKey, 1);
db.add(graphId, numbersKey, 2);
int[] numbers = db.getInts(graphId, numbersKey);
// => [1, 2]

// QUERIES
long[] vertexIds = database.queryElements(
  // label predicate
  label -> label == vertexLabel
);
// => [sourceVertexId, targetVertexId]
long[] confirmedIds = database.queryElements(
  // property predicate
  (db, id) -> db.is(id, confirmedKey)
);
// => [edgeId]
long[] confirmedEdgeIds = database.queryElements(
  // label and property predicates
  label -> label == edgeLabel, 
  (db, id) -> db.is(id, confirmedKey)
);
// => [edgeId]
```

## Operators 

### Frequent Subgraph Mining

#### Basic Frequent Subgraph Mining

#### Generalized Subgraph Mining

#### Characteristic Subgraph Mining
