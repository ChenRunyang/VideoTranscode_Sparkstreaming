package com.cry.QQNumCheck;

import org.apache.spark.util.AccumulatorV2;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;

import java.util.Date;
import java.util.HashMap;

public class myCollectionAccumulator {
    public static class CollectionAccumulator<T> extends AccumulatorV2<T, Date> {

        private Date date = new Date(1);//初始化为极小的日期值

        @Override
        public boolean isZero() {
            return (date.getTime()==1);
        }

        @Override
        public AccumulatorV2<T, Date> copy() {
            CollectionAccumulator<T> accumulator = new CollectionAccumulator<>();
            synchronized (accumulator) {
                accumulator.date = this.date;
            }
            return accumulator;
        }

        @Override
        public void reset() {
            date = new Date(1);
        }

        @Override
        public void add(T v) {
            if (((Date) v).after(date)) {
                date = (Date) v;
            }
        }

        @Override
        public void merge(AccumulatorV2<T, Date> other) {
            if (other instanceof CollectionAccumulator) {
                if (((CollectionAccumulator<Object>) other).date.after(this.date)) {
                    this.date = ((CollectionAccumulator<Object>) other).date;
                } else {
                    throw new UnsupportedOperationException("Can not merge");
                }
            }
        }

        @Override
        public Date value() {
            return date;
        }
    }
}
