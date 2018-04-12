# Directed Multigraph Miner (DMGM)
DMGM is a library for data mining in directed multigraphs. 
In a *directed graph* edges are directed from a *source vertex* to a *target vertex*.
In a *multigraph* there may be multiple edges between any pair of vertices. 
Although popular graph models such as the [property graph model](https://github.com/tinkerpop/blueprints/wiki/Property-Graph-Model) or [RDF](https://www.w3.org/TR/rdf-concepts/) show these features, 
most existing graph mining libraries lack of their support.
DMGM is Open Source (GPL v3), open for contributions and aims to be a general framework for practical graph mining algorithms.

DMGM supports the [Extended Property Graph Model](http://dbs.uni-leipzig.de/file/EPGM.pdf) and implements a similar approach as [Gradoop](http://www.gradoop.com). In particular, both systems use pipelines of graph operators to enable the declarative expression of [complex analytical questions](http://dbs.uni-leipzig.de/file/Graph_Mining_for_Complex_Data_Analytics.pdf) about graph-structured data. However, while Gradoop is designed for a cluster of machines without shared memory, DMGM is optimized for parallel execution on a single computer.

## Data Model
All DMGM programs require a database represented by the `PropertyGraphDb` interface. However, our model supports more than a typical [property graph](https://github.com/tinkerpop/blueprints/wiki/Property-Graph-Model) and even more than the [Extended Property Graph Model](http://dbs.uni-leipzig.de/file/EPGM.pdf). Let's start with a quick walk-through:

### Data elements
Any data element in a graph database is represented by a `long` value. Data elements are vertices, edges, graphs and graph collections.

+ **vertex**: The simplest data element; a simple id; aka node or point.
+ **edge**: A connection between two vertices, i.e., a pair of two vertex ids; aka line or arc. 
+ **graph:** A pair with two collections of vertex ids and edge ids.
+ **graph collection:** A collection of graph ids.

Note that a `long` which represents a data element may have multiple roles. For example, a data element may be vertex of a graph but represent a graph itself and edges may connect graphs with graph collections. This flexibility should be used for provenance (e.g., graph collection B was extracted from graph A) but not for algorithmic purposes.

### Labels and Properties
All data elements may have a label and arbitrary properties.

+ **label:** Any data element must have a label, i.e., a `String` that adds a semantic meaning. Examples:
  + A vertex represents a `"User"`.
  + An edge has the type `"friendship"`.
  + A graph stores a `"SocialNetwork"`.
  + A graph collections contains `"FrequentPatterns"`.
  
+ **property:** A property is a `key=value` pair, for example `name=Alice`. Any data element may have arbitrary properties. For performance reasons every property value has a type. Currently, DMGM supports the following data types:
  + primitives: `boolean`, `long`, `double`
  + arrays: `int[]`. `String[]`
  + objects: `String`, `BigDecimal`, `LocalDate`
  
+ **dictionary coding:** Internally, DMGM uses dictionary coding to store labels and property keys. Thus, the API allows the use of `String` or encoded `int` values for all methods related to labels and properties. For performance reasons encoded values should be used where possible to avoid dictionary lookups.

![DMGM Data Model](./src/media/data_model.svg)

*The illustration shows all types of data elements with ids, labels and properties.*

### Implementation and API
`InMemoryGraphDb` is the reference implementation of `PropertyGraphDb`. However, the interface is designed to support arbitrary database technologies such as relational and full-featured graph databases. Please feel free to contribute further implementations. 

```java
// CREATE DB
boolean parallelRead = true;
PropertyGraphDb database = new InMemoryGraphDb(parallelRead);

// DICTIONARY CODING
int vertexLabel = database.encode("Vertex");

// CREATE DATA ELEMENTS
long sourceVertexId = database.createVertex(vertexLabel);
long targetVertexId = database.createVertex(vertexLabel);
long edgeId = database.createEdge("Edge", sourceVertexId, targetVertexId);
long graphId = database.createGraph(
    "Graph", new long[] {sourceVertexId, targetVertexId}, new long[] {edgeId});
long collectionId = database.createCollection("Collection", new long[] {graphId});

// READ DATA ELEMENTS
SourceIdTargetId edge = database.getSourceIdTargetId(edgeId);
// => (sourceVertexId, targetVertexId)
VertexIdsEdgeIds graph = database.getVertexIdsEdgeIds(graphId);
// => ([sourceVertexId, targetVertexId], [edgeId])
long[] collection = database.getGraphIdsOfCollection(collectionId);
// => [graphId]
long[] sourceVertexContainedInGraphIds = database.getGraphIdsOfVertex(sourceVertexId);
// => [graphId]
long[] edgeContainedInGraphIds = database.getGraphIdsOfEdge(edgeId);
// => [graphId]

// LABELS
int edgeLabel = database.getLabel(edgeId);
String decoded = database.decode(edgeLabel);
// => "Edge"

// PROPERTIES
int confirmedKey = database.encode("confirmedKey");
database.set(edgeId, confirmedKey, true);
boolean edgeConfirmed = database.is(edgeId, confirmedKey);
// => true
boolean graphConfirmed = database.is(graphId, confirmedKey);
// => false

database.set(graphId, "number", 1L);
long number = database.getLong(graphId, "number");
// => 1L

int numbersKey = database.encode("numbers");
database.add(graphId, numbersKey, 1);
database.add(graphId, numbersKey, 2);
int[] numbers = database.getInts(graphId, numbersKey);
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

DMGM supports operators with different domains and co-domains. Currently, the only implemented combination is `CollectionToCollectionOperator`. The interface describes an operation that derives an output graph collection from an input graph collection. For example, frequent subgraph mining extracts a collection of frequent patterns (graphs) from an data graph collection. The following operators are already implemented:

### Graph Pattern Mining

DMGM supports different operators which extract graph patterns of interest:

#### Frequent Subgraph Mining

[Frequent subgraph mining (FSM)](https://www.cambridge.org/core/journals/knowledge-engineering-review/article/a-survey-of-frequent-subgraph-mining-algorithms/A58904230A6680001F17FCE91CB8C65F) is an algorithm that extracts a collection of patterns which are frequently supported within a collection of input graphs. In this context a pattern is a graph itself. An input graph supports a pattern if there is at least one isomorphic subgraph. [Graph isomorphism](https://en.wikipedia.org/wiki/Graph_isomorphism) is the existence of a [bijective](https://en.wikipedia.org/wiki/Bijection) mapping between vertex and edge sets of two graphs, where even source and target vertices correspond to each other. A pattern will be frequent if the number of input graphs that support it are gte a given minimum support threshold. In DMGM, the minimum support is set by a relative value such as 50%. 

DMGM's implementation is based on [gSpan](https://www.cs.ucsb.edu/~xyan/software/gSpan.htm). The algorithm was adopted to [support directed multigraphs](http://dbs.uni-leipzig.de/file/Graph_Mining_for_Complex_Data_Analytics.pdf) and parallelized using Java's stream abstraction. It considers only labels of vertices and edges. Thus, relevant property information must be integrated in labels if required, for example a vertex with label `"User"` and name=Alice can be relabeled to `"User|Alice"`. FSM is used as follows:

```java
boolean parallel = true;
float minSupport = 0.8f;
int maxEdgeCount = 4;

PropertyGraphDb database = // input database;
long inputCollectionId = // input id;

CollectionToCollectionOperator operator = new PatternMiningBuilder(database, parallel)
    .fromCollection()
    .extractFrequentSubgraphs(minSupport, maxEdgeCount)
    .simple();

long outputCollectionId = operator.apply(inputCollectionId);

for (Long patternId : database.getGraphIdsOfCollection(outputCollectionId)) {
  String canocialLabel = database.getString(patternId, DmgmConstants.PropertyKeys.DFS_CODE);
  long support = database.getLong(patternId, DmgmConstants.PropertyKeys.SUPPORT);
}
```
The code example shows the application of FSM with DMGM. There is a `PatternMiningBuilder` to instantiate a FSM operator. Besides `database` and `inputCollectionId` the operator requires three parameters:

+ `parallel`: to enable parallel execution
+ `minSupport`: to set the relative minimum support threshold (0f..1.0f)
+ `maxEdgeCount`: to limit the output pattern size (maximum number of edges >= 1)

All three parameters highly impact the runtime. Since FSM is a [NP-complete](https://en.wikipedia.org/wiki/NP-completeness) problem, one should carefully decrease `minSupport` from 1.0 and increase `maxEdgeCount` from 1.

Members of the result collection have the following properties from `DmgmConstants.PropertyKeys`:

+ `DFS_CODE`: a [canonical](https://en.wikipedia.org/wiki/Canonicalization) string representation of the pattern
+ `SUPPORT`: the absolute support of a pattern

![Frequent Subgraph Mining example](./src/media/fsm_example.svg)

*The illustration shows a FSM example with a minimum support threshold of 66%.*

#### Characteristic Subgraph Mining

[Characteristic Subgraph Mining (CSM)](https://dbs.uni-leipzig.de/file/CCP.pdf) is a descendant of FSM. Suppose a graph collection can be categorized into two kinds of graphs, let's say As and Bs. In such cases not globally frequent but significant patterns of As and Bs can be point of interest. To calculate a significance measure the frequency of a pattern must be known for As and Bs although it might be only frequent in A. There are two naive solutions to this problem: 

+ Either determine all pattern frequencies in all categories without a support threshold 
+ or extract only frequent patterns from all categories and get missing values by [subgraph isomorphism testing](https://en.wikipedia.org/wiki/Subgraph_isomorphism_problem) aka graph pattern matching.

Both solutions are inefficient. DMGM implements an efficient algorithm which determines all pattern that are frequent in at least one category. Thus, input graphs must be categorized by setting the `DmgmConstants.PropertyKeys.CATEGORY` property with a `String` value. If this is only done for a part of the input graphs, a default category will be used for the remaining ones. The application is similar to FSM:

```java
CollectionToCollectionOperator operator = new PatternMiningBuilder(database, parallel)
    .fromCollection()
    .extractFrequentSubgraphs(minSupport, maxEdgeCount)
    .characteristic();

long outputCollectionId = operator.apply(inputCollectionId);

for (Long patternId : database.getGraphIdsOfCollection(outputCollectionId)) {
  String category = database.getString(patternId, DmgmConstants.PropertyKeys.CATEGORY);
  String canonicalLabel = database.getString(patternId, DmgmConstants.PropertyKeys.DFS_CODE);
  long support = database.getLong(patternId, DmgmConstants.PropertyKeys.SUPPORT);
}
```
The only difference is the final method call in the `PatternMiningBuilder`. Every extracted pattern will have `DmgmConstants.PropertyKeys.CATEGORY` property value. Patterns will be extracted redundantly but only once per category. The support property reflects the category support. If a pattern is not reported for a category, its support in this category can be considered to be 0.

![Characteristic Subgraph Mining example](./src/media/csm_example.svg)

*The illustration shows a CSM example with a minimum support threshold of 66%.*

#### Generalized Subgraph Mining
[Generalized Subgraph Mining](https://dbs.uni-leipzig.de/file/gmdfsm.pdf) is an extension to either FSM or CSM. In some scenarios labels can be associated to taxonomies and, thus, expressed by taxonomy paths such as `Germany->Saxony->Leipzig`. In this case patterns that include `Leipzig` can be infrequent while more general patterns containing `Saxony` are frequent and point of interest. DMGM supports vertex-generalized variants of FSM and CSM. Therefore, taxonomy paths must used as vertex labels in the format of `Germany_Saxony_Leipzig`, i.e., level-wise from taxonomy root to leaf and separated by `_`. Please ensure, that `_` is not part of single levels. The application is analogous to FSM/CSM:

```java
CollectionToCollectionOperator operator = new PatternMiningBuilder(database, parallel)
    .fromCollection()
    .extractFrequentSubgraphs(minSupport, maxEdgeCount)
    .generalized(); // OR .generalizedCharacteristic();
```

The result will contain all combinations across all levels.

![Generalization example](./src/media/gen_example.svg)

*The illustration shows a lattice of a pattern (top) and three generalized parents including increased support values.*

### Data Statistics
As the base for the calculation of significance measures, DMGM provides a special class of operators which extract statistics such as label support. These operators can be instantiated in the following way:

```java
StatisticsExtractor<Map<Integer, Long>> operator = new StatisticsBuilder(database, parallel)
    .fromCollection()
    .ofVertexLabels() // OR .ofEdgeLabels()
    .getSupport(generalized);

Map<Integer, Long> labelSupport = operator.apply(collectionId);
```

If the parameter `generalized` is set to `true`, the taxonomy path separator `_` will be used to determine also generalization frequencies. For example, the label `Germany_Saxony_Leipzig` will be counted three times because of the additional generalizations `Germany_Saxony` and `Germany`.
