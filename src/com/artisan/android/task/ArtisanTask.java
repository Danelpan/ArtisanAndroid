package com.artisan.android.task;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ArtisanTask {
	
	List<ArtisanWorker> workers = Collections.synchronizedList(new LinkedList<ArtisanWorker>());
	
	public ArtisanTask(){}
	
	public final <Params> ArtisanTask runOnThread(IArtisanTaskListener<Params> listener){
		runOnThread(null, listener);
		return this;
	}
	
//	public final <Params> ArtisanTask runOnThread(long timeout,IArtisanTaskListener<Params> listener){
//		runOnThread(null,timeout, listener);
//		return this;
//	}
	
	public final <Params> ArtisanTask runOnThread(Params params,IArtisanTaskListener<Params> listener){
		runOnThread(params,-1L, listener);
		return this;
	}
	
	public final <Params> ArtisanTask runOnThread(Params params,long timeout,IArtisanTaskListener<Params> listener){
		listener.onStarted(params);
		ArtisanWorker task = initialTask(params, timeout, listener);
		new Thread(task).start();
		return this;
	}
	
	public final <Params> ArtisanTask runOnThreadPool(IArtisanTaskListener<Params> listener){
		runOnThreadPool(null, listener);
		return this;
	}
	
	public final <Params> ArtisanTask runOnThreadPool(Params params,IArtisanTaskListener<Params> listener){
		runOnThreadPool(params,-1L,listener);
		return this;
	}
	
	public final <Params> ArtisanTask runOnThreadPool(long timeout,IArtisanTaskListener<Params> listener){
		runOnThreadPool(obtainExecutor(),null,timeout, listener);
		return this;
	}	
	
	public final <Params> ArtisanTask runOnThreadPool(Params params,long timeout,IArtisanTaskListener<Params> listener){
		runOnThreadPool(obtainExecutor(),params,timeout, listener);
		return this;
	}
	
	public final <Params> ArtisanTask runOnThreadPool(ExecutorService service,long timeout,IArtisanTaskListener<Params> listener){
		runOnThreadPool(service,null,timeout,listener);
		return this;
	}
	
	public final <Params> ArtisanTask runOnThreadPool(ExecutorService service,Params params,long timeout,IArtisanTaskListener<Params> listener){
		listener.onStarted(params);
		ArtisanWorker task = initialTask(params, timeout, listener);
		service.submit(task);
		return this;
	}
	
	public final void destory(){
		for (ArtisanWorker task : workers) {
			task.destory(true);
		}
		workers.clear();
	}
	
	private final  <Params> ArtisanWorker initialTask(Params params,long timeout,IArtisanTaskListener<Params> listener){
		ArtisanCallable<Params> callable = new ArtisanCallable<Params>(params, listener);
		ArtisanWorker worker = new ArtisanWorker(callable,timeout);
		addWorker(worker);
		return worker;
	}
	
	private final ExecutorService obtainExecutor(){
		return new ThreadPoolExecutor(2, obtainProcessors(),0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
	}
	
	private final int obtainProcessors() {
		int size = Runtime.getRuntime().availableProcessors();
		if(size<=0){
			size = 1;
		}
		return size * 2;
	}
	
	private void addWorker(ArtisanWorker worker){
		trimWorker();
		workers.add(worker);
	}
	
	private synchronized void trimWorker(){
		Iterator<ArtisanWorker> iterator = workers.iterator();
		while (iterator.hasNext()) {
			ArtisanWorker worker = iterator.next();
			if (worker.isDone() || worker.isCancelled() || worker.isDestory()) {
				iterator.remove();
			}
		}
	}
}
