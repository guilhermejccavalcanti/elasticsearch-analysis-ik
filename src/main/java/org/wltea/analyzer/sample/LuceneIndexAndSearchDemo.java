/**
 * IK ????  ?? 5.0
 * IK Analyzer release 5.0
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ???????(linliangyi2005@gmail.com)??
 * ???? 2012???????
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 * 
 */
package org.wltea.analyzer.sample;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * ??IKAnalyzer??Lucene????????
 * 2012-3-2
 * 
 * ?????Lucene4.0 API???
 *
 */
public class LuceneIndexAndSearchDemo {

    public static final ESLogger logger = Loggers.getLogger("ik-analyzer");

    /**
	 * ???
	 * ???????????????????
	 * @param args
	 */
    public static void main(String[] args) {
        String fieldName = "text";
        String text = "IK Analyzer?????????????????????????????????????????????";
        Analyzer analyzer = new IKAnalyzer(true);
        Directory directory = null;
        IndexWriter iwriter = null;
        IndexReader ireader = null;
        IndexSearcher isearcher = null;
        try {
            directory = new RAMDirectory();
            IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
            iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);
            Document doc = new Document();
            doc.add(new StringField("ID", "10000", Field.Store.YES));
            doc.add(new TextField(fieldName, text, Field.Store.YES));
            iwriter.addDocument(doc);
            iwriter.close();
            ireader = DirectoryReader.open(directory);
            isearcher = new IndexSearcher(ireader);
            String keyword = "???????";
            QueryParser qp = new QueryParser(fieldName, analyzer);
            qp.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query query = qp.parse(keyword);
            System.out.println("Query = " + query);
            TopDocs topDocs = isearcher.search(query, 5);
            System.out.println("???" + topDocs.totalHits);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < topDocs.totalHits; i++) {
                Document targetDoc = isearcher.doc(scoreDocs[i].doc);
                System.out.println("???" + targetDoc.toString());
            }
        } catch (CorruptIndexException e) {
            logger.error(e.getMessage(), e);
        } catch (LockObtainFailedException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (ireader != null) {
                try {
                    ireader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
