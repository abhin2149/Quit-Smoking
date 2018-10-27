
package com.example.abhinav.quitsmoking.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quotes {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private Object next;
    @SerializedName("previous")
    @Expose
    private Object previous;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    /**
     * No args constructor for use in serialization
     */
    public Quotes() {
    }

    /**
     * @param results
     * @param previous
     * @param count
     * @param next
     */
    public Quotes(Integer count, Object next, Object previous, List<Result> results) {
        super();
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getNext() {
        return next;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    public static class Result implements Serializable {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("quote")
        @Expose
        private String quote;
        @SerializedName("quote_id")
        @Expose
        private String quoteId;

        /**
         * No args constructor for use in serialization
         */
        public Result() {
        }

        /**
         * @param quoteId
         * @param quote
         * @param url
         */
        public Result(String url, String quote, String quoteId) {
            super();
            this.url = url;
            this.quote = quote;
            this.quoteId = quoteId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getQuote() {
            return quote;
        }

        public void setQuote(String quote) {
            this.quote = quote;
        }

        public String getQuoteId() {
            return quoteId;
        }

        public void setQuoteId(String quoteId) {
            this.quoteId = quoteId;
        }

    }
}