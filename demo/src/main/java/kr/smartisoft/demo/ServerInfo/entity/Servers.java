package kr.smartisoft.demo.Perfomance.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@Table(name = "tbl_servers")
@EntityListeners(AuditingEntityListener.class)
public class Servers {

    @Id
    @Column(columnDefinition = "INTEGER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "server_name")
    private int serverName;

}