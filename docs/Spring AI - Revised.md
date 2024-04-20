
# Chat Completion API

The Chat Completion API allows developers to integrate AI-powered chat completion capabilities into their applications. It leverages pre-trained language models, such as GPT (Generative Pre-trained Transformer), to generate human-like responses to user inputs in natural language.

The API typically sends a prompt or partial conversation to the AI model, which then generates a completion or continuation of the conversation based on its training data and understanding of natural language patterns. The completed response is then returned to the application, which can be presented to the user or used for further processing.

The `Spring AI Chat Completion API` is designed to be a simple and portable interface for interacting with various [AI Models](https://docs.spring.io/spring-ai/reference/concepts.html#_models), allowing developers to switch between models with minimal code changes. This design aligns with Spring’s philosophy of modularity and interchangeability.

Also, with the help of companion classes like `Prompt` for input encapsulation and `ChatResponse` for output handling, the Chat Completion API unifies the communication with AI Models. It manages the complexity of request preparation and response parsing, offering a direct and simplified API interaction.

Open AI
Ollama
Azure OpenAI
Amazon Bedrock
HuggingFace
Google VertexAI
Mistrial AI

# Embeddings API

The `EmbeddingClient` interface is designed for straightforward integration with embedding models in AI and machine learning. Its primary function is to convert text into numerical vectors, commonly called embeddings. These embeddings are crucial for tasks such as semantic analysis and text classification.

The design of the EmbeddingClient interface centers around two primary goals:

- **Portability**: This interface ensures easy adaptability across various embedding models. It allows developers to switch between different embedding techniques or models with minimal code changes. This design aligns with Spring’s philosophy of modularity and interchangeability.
    
- **Simplicity**: EmbeddingClient simplifies the process of converting text to embeddings. Providing straightforward methods like embed(String text) and embed(Document document) takes the complexity out of dealing with raw text data and embedding algorithms. This design choice makes it easier for developers, primarily those new to AI, to utilize embeddings in their applications without delving deep into the underlying mechanics.

# Image Generation API

The `Spring Image Generation API` is designed to be a simple and portable interface for interacting with various [AI Models](https://docs.spring.io/spring-ai/reference/concepts.html#_models) specialized in image generation. It allows developers to switch between different image-related models with minimal code changes. This design aligns with Spring’s philosophy of modularity and interchangeability, ensuring developers can quickly adapt their applications to different AI capabilities related to image processing.

Additionally, with the support of companion classes like `ImagePrompt` for input encapsulation and `ImageResponse` for output handling, the Image Generation API unifies the communication with AI Models dedicated to image generation. It manages the complexity of request preparation and response parsing, offering a direct and simplified API interaction for image-generation functionalities.

The Spring Image Generation API is built on top of the Spring AI `Generic Model API`, providing image-specific abstractions and implementations.

# Transcription API

Spring AI provides support for OpenAI’s Transcription API. Spring will provide a common AudioTranscriptionClient interface that will be extracted when additional transcription providers are implemented.

# Vector Databases

A vector database is a specialized database that plays an essential role in AI applications.

In vector databases, queries differ from traditional relational databases. Instead of exact matches, they perform similarity searches. When given a vector as a query, a vector database returns vectors that are “similar” to the query vector. Further details on how this similarity is calculated at a high level are provided in a [Vector Similarity](https://docs.spring.io/spring-ai/reference/api/vectordbs/understand-vectordbs.html#vectordbs-similarity).

Vector databases integrate data with AI models. The first step is to load your data into a vector database. Then, a set of similar documents is first retrieved when a user query is sent to the AI model. These documents serve as the context for the user’s question and are sent to the AI model and the user’s query. This technique is known as [Retrieval Augmented Generation (RAG)](https://docs.spring.io/spring-ai/reference/concepts.html#concept-rag).

The following sections describe the Spring AI interface for using multiple vector database implementations and some high-level sample usage.

The last section is intended to demystify the underlying approach of similarity searching in vector databases.

- [Azure AI Service](https://docs.spring.io/spring-ai/reference/api/vectordbs/azure.html)
- [Chroma](https://docs.spring.io/spring-ai/reference/api/vectordbs/chroma.html)
- [Milvus](https://docs.spring.io/spring-ai/reference/api/vectordbs/milvus.html)
- [Neo4j](https://docs.spring.io/spring-ai/reference/api/vectordbs/neo4j.html)
- [PGvector](https://docs.spring.io/spring-ai/reference/api/vectordbs/pgvector.html)
- [Weaviate](https://docs.spring.io/spring-ai/reference/api/vectordbs/weaviate.html)
- [Redis](https://docs.spring.io/spring-ai/reference/api/vectordbs/redis.html)
- [Pinecone](https://docs.spring.io/spring-ai/reference/api/vectordbs/pinecone.html)
- [Qdrant](https://docs.spring.io/spring-ai/reference/api/vectordbs/qdrant.html)
# Function Calling API

The integration of function support in AI models, such as ChatGPT, permits the model to request the execution of client-side functions, thereby accessing necessary information or performing tasks dynamically as required.

Spring AI currently supports Function invocation for the following AI Models.

- OpenAI: Refer to the [Open AI function invocation docs](https://docs.spring.io/spring-ai/reference/api/clients/functions/openai-chat-functions.html).
    
- VertexAI Gemini: Refer to the [Vertex AI Gemini function invocation docs](https://docs.spring.io/spring-ai/reference/api/clients/functions/vertexai-gemini-chat-functions.html).
    
- Azure OpenAI: Refer to the [Azure OpenAI function invocation docs](https://docs.spring.io/spring-ai/reference/api/clients/functions/azure-open-ai-chat-functions.html).
    
- Mistral AI: Refer to the [Mistral AI function invocation docs](https://docs.spring.io/spring-ai/reference/api/clients/functions/mistralai-chat-functions.html).

# Prompts

Prompts are the inputs that guide an AI model in generating specific outputs. The design and phrasing of these prompts significantly influence the model’s responses.

At the lowest level of interaction with AI models in Spring AI, handling prompts in Spring AI is somewhat similar to managing the "View" in Spring MVC. This involves creating extensive text with placeholders for dynamic content. These placeholders are then replaced based on user requests or other application code. Another analogy is an SQL statement that contains placeholders for certain expressions.

As Spring AI evolves, it will introduce higher levels of abstraction for interacting with AI models. The foundational classes described in this section can be likened to JDBC regarding their role and functionality. The `ChatClient` class, for instance, is analogous to the core JDBC library in the JDK. Building upon this, Spring AI can provide helper classes similar to `JdbcTemplate`, Spring Data Repositories, and, eventually, more advanced constructs like ChatEngines and Agents that consider past interactions with the model.

The structure of prompts has evolved within the AI field. Initially, prompts were simple strings. Over time, they added placeholders for specific inputs, like "USER: "which the AI model recognizes. OpenAI has introduced more structure to prompts by categorizing multiple message strings into distinct roles before the AI model processes them.

# Output Parsers

The `OutputParser` interface allows you to obtain structured output, for example, mapping the output to a Java class or an array of values from the String-based output of AI Models.

You can think of it in terms similar to Spring JDBC’s concept of a `RowMapper` or `ResultSetExtractor`. Developers want to quickly turn results from an AI model into data types that can be passed to other functions and methods in their application. The `OutputParser` helps achieve that goal.

# ETL Pipeline

The Extract, Transform, and Load (ETL) framework is the backbone of data processing within the Retrieval Augmented Generation (RAG) use case.

The ETL pipeline orchestrates the flow from raw data sources to a structured vector store, ensuring data is in the optimal format for retrieval by the AI model.

The RAG use case is text to augment the capabilities of generative models by retrieving relevant information from a body of data to enhance the quality and relevance of the generated output.

# Generic Model API

The Generic Model API was created to provide a foundation for all AI Model clients. This makes it easy to contribute new AI Model support to Spring AI by following a standard pattern. The following sections walk through this API.
