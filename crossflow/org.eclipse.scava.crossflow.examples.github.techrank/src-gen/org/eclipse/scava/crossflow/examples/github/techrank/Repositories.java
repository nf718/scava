package org.eclipse.scava.crossflow.examples.github.techrank;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.scava.crossflow.runtime.Workflow;
import org.eclipse.scava.crossflow.runtime.Job;
import org.eclipse.scava.crossflow.runtime.Stream;

public class Repositories extends Stream<Repository> {
		
	public Repositories(Workflow workflow) throws Exception {
		super(workflow);
		
		pre.put("RepositorySearchDispatcher", (ActiveMQDestination) session.createQueue("RepositoriesPre.RepositorySearchDispatcher." + workflow.getInstanceId()));
		destination.put("RepositorySearchDispatcher", (ActiveMQDestination) session.createQueue("RepositoriesDestination.RepositorySearchDispatcher." + workflow.getInstanceId()));
		post.put("RepositorySearchDispatcher", (ActiveMQDestination) session.createQueue("RepositoriesPost.RepositorySearchDispatcher." + workflow.getInstanceId()));
		
		for (String consumerId : pre.keySet()) {
			ActiveMQDestination preQueue = pre.get(consumerId);
			ActiveMQDestination destQueue = destination.get(consumerId);
			ActiveMQDestination postQueue = post.get(consumerId);
			
			if (workflow.isMaster()) {
				MessageConsumer preConsumer = session.createConsumer(preQueue);
				consumers.add(preConsumer);
				preConsumer.setMessageListener(new MessageListener() {
	
					@Override
					public void onMessage(Message message) {
						try {
							Job job = (Job) ((ObjectMessage) message).getObject();
							if (workflow.getCache() != null && workflow.getCache().hasCachedOutputs(job)) {
								for (Job output : workflow.getCache().getCachedOutputs(job)) {
									
									if (output.getDestination().equals("RepositorySearches")) {
										((TechrankWorkflow) workflow).getRepositorySearches().send((RepositorySearch) output, consumerId);
									}
									
								}
							} else {
								MessageProducer producer = session.createProducer(destQueue);
								producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
								producer.send(message);
								producer.close();
							}
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					
				});
				
				MessageConsumer destinationConsumer = session.createConsumer(destQueue);
				consumers.add(destinationConsumer);
				destinationConsumer.setMessageListener(new MessageListener() {
	
					@Override
					public void onMessage(Message message) {
						try {
							ObjectMessage objectMessage = (ObjectMessage) message;
							Job job = (Job) objectMessage.getObject();
							if (workflow.getCache() != null && !job.isCached()) {
								workflow.getCache().cache(job);
							}
							MessageProducer producer = session.createProducer(postQueue);
							producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
							producer.send(message);
							producer.close();
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					
				});
			}
		}
	}
	
	public void addConsumer(RepositoriesConsumer consumer, String consumerId) throws Exception {
	
		ActiveMQDestination postQueue = post.get(consumerId);
		
		//only connect if the consumer exists (for example it will not in a master_bare situation)
		if(consumer!=null) {
		
			MessageConsumer messageConsumer = session.createConsumer(postQueue);
			consumers.add(messageConsumer);
			messageConsumer.setMessageListener(new MessageListener() {
		
				@Override
				public void onMessage(Message message) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					try {
						Repository repository = (Repository) objectMessage.getObject();
						consumer.consumeRepositoriesWithNotifications(repository);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			});
		}
	
	}

}