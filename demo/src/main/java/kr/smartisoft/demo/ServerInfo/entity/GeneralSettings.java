package kr.smartisoft.demo.ServerInfo.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tbl_general_settings")
public class GeneralSettings {

    @Id
    @Column(columnDefinition = "INTEGER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "interval_time")
    private Integer intervalTime;

}