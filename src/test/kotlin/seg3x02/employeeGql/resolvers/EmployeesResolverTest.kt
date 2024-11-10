package seg3x02.employeeGql.resolvers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository

class EmployeesResolverTest {
    
    private lateinit var employeeRepository: EmployeesRepository
    private lateinit var employeesResolver: EmployeesResolver
    
    @BeforeEach
    fun setUp() {
        employeeRepository = mockk()
        employeesResolver = EmployeesResolver(employeeRepository)
    }
    
    @Test
    fun `test query employees returns all employees`() {
        // Arrange
        val expectedEmployees = listOf(
            Employee(
                name = "Test Name",
                dateOfBirth = "2001-11-11",
                city = "Ottawa",
                salary = 8000f,
                gender = "F",
                email = "test@test.com"
            ).apply { id = "1" }
        )
        
        every { employeeRepository.findAll() } returns expectedEmployees
        
        // Act
        val result = employeesResolver.employees()
        
        // Assert
        assertEquals(expectedEmployees, result)
        verify(exactly = 1) { employeeRepository.findAll() }
    }
    
    @Test
    fun `test mutation newEmployee creates and returns new employee`() {
        // Arrange
        val input = CreateEmployeeInput(
            name = "John Doe",
            dateOfBirth = "1950-05-05",
            city = "Ottawa",
            salary = 8000f,
            gender = "F",
            email = "test@test.com"
        )
        
        val expectedEmployee = Employee(
            name = input.name,
            dateOfBirth = input.dateOfBirth,
            city = input.city,
            salary = input.salary,
            gender = input.gender,
            email = input.email
        ).apply { id = "3" }
        
        every { employeeRepository.save(any()) } returns expectedEmployee
        
        // Act
        val result = employeesResolver.newEmployee(input)
        
        // Assert
        assertEquals(expectedEmployee, result)
        verify(exactly = 1) { employeeRepository.save(any()) }
    }
}