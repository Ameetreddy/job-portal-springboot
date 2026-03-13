package com.example.demo.entity;


import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="users_type")
public class UserType {

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		
		private int userTypeId;
		
		
		private String userTypeName;
		
		
		@OneToMany(mappedBy = "userTypeId", fetch = FetchType.LAZY)
		private List<User> users;

		
		public UserType() {
			
		}


		public UserType(int userTypeId, String userTypeName, boolean isActive, List<User> users) {
			super();
			this.userTypeId = userTypeId;
			this.userTypeName = userTypeName;
			
			this.users = users;
		}


		public int getUserTypeId() {
			return userTypeId;
		}


		public void setUserTypeId(int userTypeId) {
			this.userTypeId = userTypeId;
		}


		public String getUserTypeName() {
			return userTypeName;
		}

		public void setUserTypeName(String userTypeName) {
			this.userTypeName = userTypeName;
		}
		
		public List<User> getUsers() {
			return users;
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

		@Override
		public String toString() {
			return "UserType [userTypeId=" + userTypeId + ", userTypeName=" + userTypeName + "]";
		}
			
}