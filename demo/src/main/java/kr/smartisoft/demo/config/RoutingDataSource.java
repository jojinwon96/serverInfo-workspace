//package kr.smartisoft.demo.config;
//
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//public class RoutingDataSource extends AbstractRoutingDataSource {
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        // readOnly 속성을 구별하여 key를 반환
//        return (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) ? "slave" : "master";
//    }
//}
