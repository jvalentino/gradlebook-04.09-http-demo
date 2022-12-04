package com.blogspot.jvalentino.gradle

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification
import spock.lang.Subject

class BloggerTaskTestSpec extends Specification {

    @Subject
    BloggerTask task
    Project project
    
    def setup() {
        Project p = ProjectBuilder.builder().build()
        task = p.task('blogger', type:BloggerTask)
        task.instance = Mock(BloggerTask)
        project = Mock(ProjectInternal)
    }
    
    void "Test perform"() {
        given:
        Map properties = ['url':'https://foo.com']
        URL.class.metaClass.getText = { 
            new File('src/test/resources/blogger.json').text 
        }
        
        when:
        task.perform()
        
        then:
        1 * task.instance.project >> project
        1 * project.properties >> properties
        
        and:
        task.title == 'Blah Blah Blah'
        task.postDateTimes.size() == 5
        task.postDateTimes.get(0) == '2015-04-06T09:17:00.002-06:00'
        task.postDateTimes.get(1) == '2015-04-05T09:17:00.002-06:00'
        task.postDateTimes.get(2) == '2015-04-04T09:17:00.002-06:00'
        task.postDateTimes.get(3) == '2015-04-03T09:17:00.002-06:00'
        task.postDateTimes.get(4) == '2015-04-02T09:17:00.002-06:00'
    }
}
