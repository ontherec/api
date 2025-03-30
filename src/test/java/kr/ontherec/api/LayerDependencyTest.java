package kr.ontherec.api;


import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "kr.ontherec.api.modules", importOptions = DoNotIncludeTests.class)
public class LayerDependencyTest {

	@ArchTest
	static final ArchRule layerDependenciesRule = layeredArchitecture().consideringAllDependencies()
			.layer("Presentation").definedBy("..presentation..")
			.layer("Application").definedBy("..application..")
			.layer("Entity").definedBy("..entity..")
			.layer("Dto").definedBy("..dto..")
			.layer("Dao").definedBy("..dao..")
			.layer("Exception").definedBy("..exception..")

			.whereLayer("Presentation").mayNotBeAccessedByAnyLayer()
			.whereLayer("Application").mayOnlyBeAccessedByLayers("Presentation")
			.whereLayer("Dao").mayOnlyBeAccessedByLayers("Presentation", "Application")
			.whereLayer("Dto").mayOnlyBeAccessedByLayers("Presentation", "Application")
			.whereLayer("Entity").mayOnlyBeAccessedByLayers("Presentation", "Application", "Dao", "Dto")
			.whereLayer("Entity").mayNotAccessAnyLayer()
			.whereLayer("Exception").mayNotAccessAnyLayer()

            .ignoreDependency(
					DescribedPredicate.alwaysTrue(),
					Predicates.resideOutsideOfPackages("kr.ontherec.modules..")
			);


	@ArchTest
	ArchRule chatCycleCheck = slices().matching("kr.ontherec.api.modules.(*)..")
			.should().beFreeOfCycles();
}
