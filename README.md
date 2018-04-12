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
  
+ *dictionary coding:* Interally, DMGM uses dictionary coding to store labels and property keys. Thus, the API allows the use of `String` or encoded `int` values for all methods related to labels and properties. For performance reasones once should use encoded values where possible to avoid dictionary lookups.

## Operators 

### Frequent Subgraph Mining

#### Basic Frequent Subgraph Mining

#### Generalized Subgraph Mining

#### Characteristic Subgraph Mining
