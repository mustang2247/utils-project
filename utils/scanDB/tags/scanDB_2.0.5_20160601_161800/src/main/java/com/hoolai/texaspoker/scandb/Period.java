/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author Cedric(TaoShuang)
 * @create 2012-10-9
 */
public class Period {
	/**
	 * 还没到这个日期呢
	 */
	public transient static final long PRIOR_TO_THE_DATE_STATE = -1L; 
	
	private transient static final SimpleDateFormat DEFAULT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public transient static final Period NULL = new Period(0) {
		public void addPeriod(long period) {
			throw new UnsupportedOperationException();
		};
		
		public void addPeriod(long period, boolean updateStart) {
			throw new UnsupportedOperationException();
		};
		
		public void toFail() {
			throw new UnsupportedOperationException();
		};
	};
	
	public transient static final Period ETERNAL = new Period("1970-1-1 00:00:00", "2100-1-1 00:00:00") {
		public void addPeriod(long period) {
			throw new UnsupportedOperationException();
		};
		
		public void addPeriod(long period, boolean updateStart) {
			throw new UnsupportedOperationException();
		};
		
		public void toFail() {
			throw new UnsupportedOperationException();
		};
	};
	
	private long fromTime;
	private long period;
	
	public Period(String fromTime, String toTime, String formatter) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatter);
			this.fromTime = format.parse(fromTime).getTime();
			this.period = format.parse(toTime).getTime() - this.fromTime;
			if (period < 0) {
				throw new RuntimeException("toTime is less than fromTime!");
			}
		} catch (ParseException e) {
			Log.e(e.getMessage(), e);
		} catch (Exception e) {
			Log.e(e.getMessage(), e);
		}
	}
	
	public Period(String fromTime, String toTime) {
		try {
			this.fromTime = DEFAULT_FORMATTER.parse(fromTime).getTime();
			this.period = DEFAULT_FORMATTER.parse(toTime).getTime() - this.fromTime;
			if (period < 0) {
				throw new RuntimeException("toTime is less than fromTime!");
			}
		} catch (ParseException e) {
			Log.e(e.getMessage(), e);
		}
	}
	
	/**
	 * 利用PeriodHelper计算出时间，然后fromTime是{@link System#currentTimeMillis()}
	 * 
	 * @param period
	 */
	public Period(long period) {
		this.fromTime = System.currentTimeMillis();
		this.period = period;
	}
	
	/**
	 * 追加持续时间
	 * 
	 * @param period
	 */
	public void addPeriod(long period) {
		addPeriod(period, false);
	}
	
	public void addPeriod(Period period) {
		addPeriod(period.interval(), false);
	}
	
	public void addPeriod(Period period, boolean updateStart) {
		addPeriod(period.interval(), updateStart);
	}
	
	public void addPeriod(long period, boolean updateStart) {
		if (updateStart) {
			this.fromTime = System.currentTimeMillis();
			this.period = 0L;
		}
		this.period += period;
	}
	
	/**
	 * 返回是否现在是在两者的期间以内
	 * 
	 * @return
	 */
	public boolean isInNow() {
		long delta = System.currentTimeMillis() - this.fromTime;
		return delta >= 0 && delta < this.period;
	}
	
	/**
	 * 返回剩余的时间
	 * 
	 * @return -1 说明还没有开始，开始时间在未来; 0 说明没有持续时间了
	 */
	public long period() {
		long delta = System.currentTimeMillis() - this.fromTime;
		if (delta < 0) {
			// 说明还未开始
			return PRIOR_TO_THE_DATE_STATE;
		} else {
			// 说明已经开始持续
			long last = period - delta;
			if (last > 0) {
				return last;
			} else {
				return 0L;
			}
		}
	}
	
	/**
	 * 失效
	 */
	public void toFail() {
		this.period = 0L;
	}
	
	/**
	 * 总计需要的时间
	 * @return
	 */
	public long interval() {
		return this.period;
	}
	
	/**
	 * 时间计算者
	 * 
	 * 参数都是大于0的任意值
	 * 
	 * @author Cedric(TaoShuang)
	 * @create 2012-11-5
	 */
	public static class PeriodHelper {
		
		/**
		 * @param days
		 * @param hours
		 * @param minutes
		 * @param seconds
		 * @return 如果参数不正常则返回-1，否则返回一个期间值。
		 */
		public static long cacl(int days, int hours, int minutes, int seconds) {
			if (checkParam(days, hours, minutes, seconds)) {
				return TimeUnit.DAYS.toMillis(days) + TimeUnit.HOURS.toMillis(hours)
						+ TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
			} else {
				return -1;
			}
		}
		
		public static long cacl(int hours, int minutes, int seconds) {
			return cacl(0, hours, minutes, seconds);
		}
		
		public static long cacl(int minutes, int seconds) {
			return cacl(0, 0, minutes, seconds);
		}
		
		public static long cacl(int seconds) {
			return cacl(0, 0, 0, seconds);
		}
		
		private static boolean checkParam(int days, int hours, int minutes, int seconds) {
			if (days < 0) {
				return false;
			}
			if (hours < 0) {
				return false;
			}
			if (minutes < 0) {
				return false;
			}
			if (seconds < 0) {
				return false;
			}
			
			return true;
		}
	}
}
