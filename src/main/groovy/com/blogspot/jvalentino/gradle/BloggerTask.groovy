package com.blogspot.jvalentino.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import groovy.json.JsonSlurper

/**
 * <p>A Task.</p>
 * @author jvalentino2
 */
@SuppressWarnings(['Println', 'DuplicateStringLiteral'])
class BloggerTask extends DefaultTask {

    BloggerTask instance = this

    String title
    List<String> postDateTimes = []

    @TaskAction
    void perform() {
        String url = instance.project.properties.url

        String jsonText = new URL(url).text

        Map json = new JsonSlurper().parseText(jsonText)

        title = json.feed.title['$t']

        int entryCount = 0
        json.feed.entry.each { Map entry ->
            if (entryCount < 5) {
                postDateTimes.add(entry.published['$t'])
            }
            entryCount++
        }

        println "Title: ${title}"
        for (String dateTime : postDateTimes) {
            println "Post Date/Time: ${dateTime}"
        }
    }
}
