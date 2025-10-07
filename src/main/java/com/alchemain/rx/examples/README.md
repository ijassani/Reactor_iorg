# Library Usage Examples

This directory contains simple examples showing how to use all the major libraries in the Reactor project.

## Overview

The Reactor project uses several libraries, and these examples demonstrate basic usage patterns for each one:

### Core Libraries Used

1. **Jackson** (JSON processing)
2. **Google Guava** (Utilities)
3. **Apache Commons IO** (File operations)
4. **Lombok** (Code generation)
5. **SLF4J + Logback** (Logging)
6. **Micrometer** (Metrics)
7. **Akka** (Actor system)
8. **Jedis** (Redis client)
9. **MongoDB Driver** (Database)
10. **Elasticsearch** (Search engine)
11. **Apache HTTP Components** (REST client)
12. **Mockito** (Testing/Mocking)

## Example Files

### 1. LibraryUsageExamples.java
**Main comprehensive example showing usage of all libraries**

```bash
# To run this example:
cd d:\Alchemain\Reactor_iorg
mvn compile exec:java -Dexec.mainClass="com.alchemain.rx.examples.LibraryUsageExamples"
```

**What it demonstrates:**
- JSON processing with Jackson
- String utilities with Guava
- File operations with Commons IO
- Lombok data classes and builders
- Structured logging with SLF4J
- Metrics collection with Micrometer
- Actor system with Akka
- Redis operations (requires Redis server)
- MongoDB operations (requires MongoDB server)
- Elasticsearch operations (requires ES server)

### 2. MockitoExamplesTest.java
**Unit testing examples with Mockito**

```bash
# To run this test:
mvn test -Dtest=MockitoExamplesTest
```

**What it demonstrates:**
- Basic mocking and verification
- Stubbing method calls
- Argument matchers
- Verification with different constraints
- Mocking service classes for unit tests

### 3. ConfigurationExamples.java
**Configuration and properties handling**

```bash
# To run this example:
mvn compile exec:java -Dexec.mainClass="com.alchemain.rx.examples.ConfigurationExamples"
```

**What it demonstrates:**
- Reading properties from files
- JSON configuration patterns
- Loading resources from classpath
- Configuration validation

### 4. RestClientExamples.java
**HTTP client usage with Apache HTTP Components**

```bash
# To run this example:
mvn compile exec:java -Dexec.mainClass="com.alchemain.rx.examples.RestClientExamples"
```

**What it demonstrates:**
- GET, POST, PUT, DELETE requests
- JSON payload handling
- Error handling and timeouts
- Custom headers
- Complex JSON response processing

## Running the Examples

### Prerequisites

1. **Java 8** (as specified in pom.xml)
2. **Maven** for building and running
3. **Optional services** (for full functionality):
   - Redis server on localhost:6379
   - MongoDB server on localhost:27017
   - Elasticsearch server on localhost:9300

### Quick Start

1. **Compile the project:**
   ```bash
   cd d:\Alchemain\Reactor_iorg
   mvn clean compile
   ```

2. **Run the main library examples:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.alchemain.rx.examples.LibraryUsageExamples"
   ```

3. **Run the configuration examples:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.alchemain.rx.examples.ConfigurationExamples"
   ```

4. **Run the REST client examples:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.alchemain.rx.examples.RestClientExamples"
   ```

5. **Run the Mockito tests:**
   ```bash
   mvn test -Dtest=MockitoExamplesTest
   ```

## Library-Specific Notes

### Lombok
The examples use `@Data` and `@Builder` annotations. Make sure your IDE has Lombok plugin installed:
- **IntelliJ IDEA**: Install Lombok plugin and enable annotation processing
- **Eclipse**: Install Lombok JAR and restart Eclipse
- **VS Code**: Install Lombok extension

### Akka Actors
The Actor examples show basic message passing. The Actor system is automatically terminated after examples run.

### Database Connections
MongoDB, Redis, and Elasticsearch examples include connection error handling. They will log warnings if the services are not available, but won't crash the examples.

### HTTP Client
REST client examples use a public testing API (jsonplaceholder.typicode.com) so they should work without additional setup.

### Metrics
Micrometer examples show basic counter and timer usage. In a real application, you'd configure a metrics registry to export to monitoring systems.

## Integration with Existing Code

These examples show patterns that are already used throughout the Reactor codebase:

- **PropertiesUtil** - Used for configuration management
- **RestClientFactory** - Used for HTTP calls in delegates
- **JsonProvider** - Used for JSON processing throughout the system
- **ReactorCore** - Uses Akka actors for message processing
- **MongoModule** - Shows MongoDB connection setup
- **SearchModule** - Shows Elasticsearch connection setup

## Troubleshooting

### Common Issues

1. **Compilation errors**: Make sure you're using Java 8 and Maven can download dependencies
2. **Lombok not working**: Install IDE plugin and enable annotation processing
3. **Database connection errors**: These are expected if services aren't running locally
4. **Missing reactor.properties**: The configuration examples handle this gracefully

### Running Individual Examples

You can also run individual methods by modifying the main methods in each example class to call only specific demonstration methods.

## Next Steps

After understanding these basic patterns, you can:

1. Look at the existing delegates in `com.alchemain.rx.internal.delegates` to see real usage
2. Examine the REST services in `com.alchemain.rx.rest` for web layer patterns
3. Study the actor system implementation in `com.alchemain.rx.internal`
4. Review the initialization code in `com.alchemain.rx.init` for Guice setup patterns

These examples provide the foundation for understanding how each library is used within the larger Reactor messaging system architecture.