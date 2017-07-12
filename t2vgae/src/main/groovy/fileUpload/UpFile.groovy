package fileUpload;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileItemFactory;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class UpFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private final String UPLOAD_DIRECTORY = "C:/data";

	protected void doPost(HttpServletRequest request,
             HttpServletResponse response) throws ServletException, IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

  	    System.out.println("in  upload doppost ");
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter;
	
		String s = "failed to parse file ";
		try {
			iter = upload.getItemIterator(request);
			FileItemStream fileItem = iter.next();
			InputStream i = fileItem.openStream();
			
			//println " i.text " +i.text

			Tika t = new Tika();			
			s = t.parseToString(i);

		} catch (FileUploadException e1) {
		
			println "e1 in tike $e1"
			e1.printStackTrace();
			s = "#error processing file"
		} catch (TikaException e) {
			s = "#error tika error processing file"
			e.printStackTrace();
		}
		
		response.getWriter().println( s); 		

	//	GetJSONpairs gt = new GetJSONpairs();

	}
}