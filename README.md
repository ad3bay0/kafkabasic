# 🚀 Modern Kafka & Spring Boot 3 Demo

An event-driven application demonstrating **Spring Boot 3**, **Java 25**, and **Apache Kafka (KRaft Mode)**.

This project implements a sample "Payment Processing" flow:

*   **Producer (API):** Receives an HTTP POST request and publishes a message.
*   **Cluster:** A resilient **3-Node Kafka Cluster** (No Zookeeper).
*   **Consumer:** A Spring listener that manually deserializes JSON for maximum safety.

---

## 🛠 Prerequisites

*   **Java 25** (or JDK 21+)
*   **Docker & Docker Compose**
*   **Maven**

---

## 🏗️ Step 1: Start the Infrastructure

We use a **3-Node Kafka Cluster** running in **KRaft mode** (Raft Consensus, no Zookeeper).

### 1. Start the Cluster
Spin up the 3 brokers (`kafka1`, `kafka2`, `kafka3`):

```bash
docker-compose up -d
```

### 2. Verify
Check that all 3 containers are running:

```bash
docker-compose ps
```

*   **Broker 1** listens on: `localhost:9092`
*   **Broker 2** listens on: `localhost:9094`
*   **Broker 3** listens on: `localhost:9096`

---

## 🏃‍♂️ Step 2: Run the Application

You can run the app directly from your IDE (IntelliJ/Eclipse) or via the terminal.

```bash
./mvnw spring-boot:run
```

Once started, the application connects to all three brokers for failover safety.

---

## 🧪 Step 3: Test the Flow

We will send a fake payment request to the API. The API will convert this into a Kafka message and send it to the `payments` topic.

### Send a Payment (Producer)
Open a new terminal window and run:

```bash
curl -X POST http://localhost:8080/api/payments \
     -H "Content-Type: application/json" \
     -d '{"userId": "user_999", "amount": 0.50, "currency": "USD"}'
```

**Expected Response:**
```plaintext
Payment sent to Kafka for processing: user_999
```

---

## 📝 Step 4: Check the Logs (Consumer)

Look at your Spring Boot application console. You should instantly see the consumer picking up the message.

Because we use **Manual Deserialization**, you will see the logic we added:

```plaintext
2026-01-01T22:11:35.983+01:00  INFO 63809 --- [kafkabasic] [ntainer#0-0-C-1] c.a.k.s.consumer.KafkaPaymentConsumer    : ---PAYMENT RECEIVED----
2026-01-01T22:11:35.983+01:00  INFO 63809 --- [kafkabasic] [ntainer#0-0-C-1] c.a.k.s.consumer.KafkaPaymentConsumer    : User ID: user_999
2026-01-01T22:11:35.983+01:00  INFO 63809 --- [kafkabasic] [ntainer#0-0-C-1] c.a.k.s.consumer.KafkaPaymentConsumer    : Amount: 0.5
2026-01-01T22:11:35.983+01:00  INFO 63809 --- [kafkabasic] [ntainer#0-0-C-1] c.a.k.s.consumer.KafkaPaymentConsumer    : Currency: USD
2026-01-01T22:11:35.983+01:00  INFO 63809 --- [kafkabasic] [ntainer#0-0-C-1] c.a.k.s.consumer.KafkaPaymentConsumer    : -----------------------
```

---

## ⚙️ Architecture Highlights

### Failover Testing
You can test the resilience of this setup by killing the leader broker while the app is running:

1.  Stop Broker 1: `docker stop kafka1`
2.  Send another cURL request.

**Result:** The app will pause briefly, switch to `localhost:9094` (Broker 2), and process the message successfully.

### Configuration
*   **Topic Defaults:** Auto-created topics now default to **3 partitions** and **3 replicas** for production safety.
*   **Security:** The Consumer reads raw String data and uses Jackson `ObjectMapper` manually to prevent "Untrusted Package" security blocks.

---

## 🧹 Cleanup

To stop the cluster and remove the containers:

```bash
docker-compose down
```

To wipe all data (start fresh):

```bash
docker-compose down -v
```
