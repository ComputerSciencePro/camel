/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.kafka;

import java.net.URISyntaxException;
import java.util.concurrent.ThreadPoolExecutor;

import kafka.message.MessageAndMetadata;

import org.apache.camel.Exchange;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Stephen Samuel
 */
public class KafkaEndpointTest {

    @Test
    public void testCreatingKafkaExchangeSetsHeaders() throws URISyntaxException {
        KafkaEndpoint endpoint = new KafkaEndpoint("kafka:localhost", "localhost", new KafkaComponent());

        MessageAndMetadata<byte[], byte[]> mm =
                new MessageAndMetadata<byte[], byte[]>("somekey".getBytes(), "mymessage".getBytes(), "topic", 4, 56);

        Exchange exchange = endpoint.createKafkaExchange(mm);
        assertEquals("somekey", exchange.getIn().getHeader(KafkaConstants.KEY));
        assertEquals("topic", exchange.getIn().getHeader(KafkaConstants.TOPIC));
        assertEquals(4, exchange.getIn().getHeader(KafkaConstants.PARTITION));
    }

    @Test
    public void creatingExecutorUsesThreadPoolSettings() throws Exception {
        KafkaEndpoint endpoint = new KafkaEndpoint("kafka:localhost", "kafka:localhost", new KafkaComponent());
        endpoint.setConsumerStreams(44);
        ThreadPoolExecutor executor = endpoint.createExecutor();
        assertEquals(44, executor.getCorePoolSize());
    }

    @Test
    public void assertSingleton() throws URISyntaxException {
        KafkaEndpoint endpoint = new KafkaEndpoint("kafka:localhost", "localhost", new KafkaComponent());
        assertTrue(endpoint.isSingleton());
    }

}
