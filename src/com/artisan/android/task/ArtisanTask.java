package com.artisan.android.task;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
	
//	public final <Params> ArtisanTask runOnThreadPool(long timeout,IArtisanTaskListener<Params> listener){
//		runOnThreadPool(THREAD_POOL_EXECUTOR,null,timeout, listener);
//		return this;
//	}	
	
	public final <Params> ArtisanTask runOnThreadPool(Params params,long timeout,IArtisanTaskListener<Params> listener){
		runOnThreadPool(THREAD_POOL_EXECUTOR,params,timeout, listener);
		return this;
	}
	
	public final <Params> ArtisanTask runOnThreadPool(Executor service,long timeout,IArtisanTaskListener<Params> listener){
		runOnThreadPool(service,null,timeout,listener);
		return this;
	}
	
	public final <Params> ArtisanTask runOnThreadPool(Executor service,Params params,long timeout,IArtisanTaskListener<Params> listener){
		listener.onStarted(params);
		ArtisanWorker task = initialTask(params, timeout, listener);
		service.execute(task);
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
	
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);

	/**
	 * An {@link Executor} that can be used to execute tasks in parallel.
	 */
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,sPoolWorkQueue, sThreadFactory);
}
