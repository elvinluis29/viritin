package org.vaadin.viritin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class CustomComparatorTest {

	private class Person{
		private String firstName;
		private String lastName;
		
		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}
		
		public String getFirstName() {
			return firstName;
		}
		
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		
		public String getLastName() {
			return lastName;
		}
		
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		@Override
		public String toString() {
			return firstName + " " + lastName;
		}
	}
	
	private class CaseInsensitiveFirstNameComparator implements Comparator<String>{
		@Override
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}
	}
	
	private class PersonContainer extends ListContainer<Person>{
		private static final long serialVersionUID = 1L;

		public PersonContainer(List<Person> people) {
			super(Person.class, people);
		}
		
		@Override
		protected Comparator<?> getUnderlyingComparator(Object property) {
			if("firstName".equals(property)){
				return new CaseInsensitiveFirstNameComparator();
			}
			
			return super.getUnderlyingComparator(property);
		}
	}
	
	private List<Person> people = Arrays.asList( 
			new Person("a", "a"),
			new Person("b", "b"),
			new Person("c", "c"),
			new Person("A", "A"),
			new Person("B", "B"),
			new Person("C", "C")
	);
	
	private String getText(Collection<?> items){
		StringBuilder sb = new StringBuilder();
		
		Iterator<?> it = items.iterator();
		
		while (it.hasNext()) {
			sb.append(it.next().toString());
			
			if(it.hasNext()){
				sb.append(", ");
			}
		}
				
		return sb.toString();
	}

	@Test
	public void testCaseInsensitiveSort(){
		PersonContainer container = new PersonContainer(people);
		
		String unsorted = getText(container.getItemIds());
		
		container.sort(new Object[]{"firstName"}, new boolean[]{true});
		
		String sorted = getText(container.getItemIds());
		
		Assert.assertTrue("a a, b b, c c, A A, B B, C C".equals(unsorted));
		Assert.assertTrue("a a, A A, b b, B B, c c, C C".equals(sorted));
	}
}
