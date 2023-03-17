package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp 
{
	private ProjectService projectService = new ProjectService();
	private Scanner scanner = new Scanner(System.in);
	private Project curProject;
	
	// @formatter:off
	private List<String> operations = List.of
			(
				"1) Add a project",
				"2) List projects",
				"3) Select a project",
				"4) Update project details",
				"5) Delete a project"
			); 
	// @formatter:on
	
	
	
	
//-------------------------------------------------------------------
	public static void main(String args[]) 
	{
		new ProjectsApp().processUserSelections();
	}
	
	
	
	
	private void processUserSelections() 
	{
		boolean done = false;
				
		while(!done)
		{
			try
			{
				int selection = getUserSelection();
				
				switch(selection)
				{
				case -1:
					done = exitMenu();
					break;	
				case 1:
					createProject();
					break;
					
				case 2:
					listProjects();
					break;
					
				case 3:
					selectProject();
					break;
					
				case 4:
					updateProjectDetails();
					break;
					
				case 5:
					deleteProject();
					break;
					
				default:
					System.out.println("\n" + selection + " is not a valid selection.  Try again.");
				}
			}
			catch(Exception e)
			{
				System.out.println("\nError: " + e + " Try again.");
			}
		}		
	}



	
	private void deleteProject() 
	{
		listProjects();
		
		Integer project_id = getIntInput("Enter the ID of the project to delete.");
		
		projectService.deleteProject(project_id);
		
		System.out.println("Project " + project_id + " was deleted successfully.");
		
		if(Objects.nonNull(curProject) && curProject.getProjectId().equals(project_id))
		{
			curProject = null;
		}
	}




	private void updateProjectDetails()
	{
		if(Objects.isNull(curProject))
		{
			System.out.println("\nPlease select a project.");
			return;
		}
		
		String project_name = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimated_hours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actual_hours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter prjoect difficulty (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter project notes [" + curProject.getNotes() + "]");
		
		Project project = new Project();
		
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(project_name) ? curProject.getProjectName() : project_name);
		project.setEstimatedHours(Objects.isNull(project_name) ? curProject.getEstimatedHours() : estimated_hours);
		project.setActualHours(Objects.isNull(project_name) ? curProject.getActualHours() : actual_hours);
		project.setDifficulty(Objects.isNull(project_name) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(project_name) ? curProject.getNotes() : notes);
		
		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());
	}




	private void selectProject()
	{
		listProjects();
		
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		curProject = null;
		
		curProject = projectService.fetchProjectById(projectId);
	}




	private void listProjects() 
	{
		List<Project> projects = projectService.fetchAllProjects(); 
		
		System.out.println("\nProjects:");
		
		for(Project i : projects)
		{
			System.out.println("  " + i.getProjectId() + ": " + i.getProjectName());
		}
	}




	private void createProject() 
	{
		String     projectName    = getStringInput ("Enter the project name");		
		BigDecimal estimatedHours = getDecimalInput("Enter estimated hours");
		BigDecimal actualHours    = getDecimalInput("Enter actual hours");
		Integer    difficulty     = getIntInput    ("Enter prjoect difficulty (1-5)");
		String     notes          = getStringInput ("enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		
		System.out.println("You have succesfully created project: " + dbProject);
	}




	private BigDecimal getDecimalInput(String prompt)
	{		
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input))
		{
			return null;
		}
		
		try
		{
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e)
		{
			throw new DbException(input + " is not a valid decimal number.");
		}		
	}




	private boolean exitMenu() 
	{		
		System.out.println("Exiting the menu.");
		return true;
	}




	private int getUserSelection()
	{
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
	}




	private Integer getIntInput(String prompt)
	{
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input))
		{
			return null;
		}
		
		try
		{
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e)
		{
			throw new DbException(input + " is not a valid number.");
		}		
	}




	private String getStringInput(String prompt)
	{
		System.out.print(prompt + ": ");
		
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}




	private void printOperations()
	{
		System.out.println("\nThese are the available selections.  Press the enter key to quit:");
		
		for(String i : operations)
		{
			System.out.println("  " + i);
		}
		
		if(Objects.isNull(curProject))
		{
			System.out.println("\nYou are not working with a project.");
		}
		else
		{
			System.out.println("\nYou are working with project: " + curProject);
		}
	}
}






