#include <iostream>
#include <QThread>
#include <QMutex>
#include <QWaitCondition>
#include <cstdlib>
#include <string>

using namespace std;

class SyncAlways : public QThread
{
	public:
	static QMutex mutex;
	static long total;

	SyncAlways(long ITERATION)
	{
		NUMBER_ITERATION = ITERATION;
	}

	static void addTotal()
	{
		mutex.lock();
		total++;
		mutex.unlock();
	}

	void run()
	{
		for (long iter = 0; iter < NUMBER_ITERATION; iter++)
		{
			addTotal();
		}
	}

	void toPrint()
	{
		cout << "Total = " << total << endl;
	}
	
	private:
		long NUMBER_ITERATION;

};

class SyncLast : public QThread
{
	public:
	static long total;
	static QMutex mutex;

		SyncLast(long ITERATION)
		{
			NUMBER_ITERATION =ITERATION;
			subtotal = 0;
		}

		static void addTotal(long sub)
		{
			mutex.lock();
			total += sub;
			mutex.unlock();
		}

		void run()
		{
			for(long iter = 0; iter < NUMBER_ITERATION; iter++)
			{
				subtotal ++;
			}
			addTotal(subtotal);
		}

		void toPrint()
		{
			cout << "Total = " << total << endl;
		}

	private:
		long subtotal;
		long NUMBER_ITERATION;
};

long SyncAlways::total = 0;
long SyncLast::total = 0;
QMutex SyncAlways::mutex;
QMutex SyncLast::mutex;

int main()//int argc, char *argv[])
{
		long NUMBER_ITERATION = 1000000;
		int NUMBER_THREAD = 8;
		cout << "Iteration = " << NUMBER_ITERATION / 100000 << " (millions) Thread = " << NUMBER_THREAD << endl;

		SyncLast * slt[NUMBER_THREAD];
		for(int tcnt = 0; tcnt < NUMBER_THREAD; tcnt++)
		{
			slt[tcnt] = new SyncLast(NUMBER_ITERATION);
		}
		for(int tcnt = 0; tcnt < NUMBER_THREAD; tcnt++)
		{
			slt[tcnt]->start();
		}
		for(int tcnt = 0; tcnt < NUMBER_THREAD; tcnt++)
		{
			slt[tcnt]->wait();
		}



		SyncAlways * saws[NUMBER_THREAD];
		for(int tcnt = 0; tcnt < NUMBER_THREAD; tcnt++)
		{
			saws[tcnt] = new SyncAlways(NUMBER_ITERATION);
		}
		for(int tcnt = 0; tcnt < NUMBER_THREAD; tcnt++)
		{
			saws[tcnt] -> start();
		}
		for(int tcnt = 0; tcnt < NUMBER_THREAD; tcnt++)
		{
			saws[tcnt]->wait();
		}
		
		cout << "SyncAlways " << endl;
		saws[0]->toPrint();
		cout << "SyncLast " << endl;
		slt[0]->toPrint();


	return 0;
}


