package dev.mouhieddine.springmvcrestexample.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author : Mouhieddine.dev
 * @since : 1/17/2021, Sunday
 **/
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vendor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
}
