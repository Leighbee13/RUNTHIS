/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.SingleThreadModel;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 *
 * @author Leigh
 */
@MultipartConfig(location = "C:\\Users\\Leigh\\Documents\\NetBeansProjects\\RUNTHIS\\src\\java\\sound")   // 50MB
public class Process extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("entering doPost");
        int count = 0;

            //tried to separate upload and doPost but it meant 
            //i had to declare count outside their respective 
            //methods i.e. threading sadtimes
        //doUpload(request.getParts());
        //Random generator = new Random();
        //get parts
        Collection<Part> parts = request.getParts();
        System.out.println("got parts");
        //create select's input stream
        InputStream programs;
        //create the string for the program location
        String program = null;
        //iterate through parts
        List<String> songs = new ArrayList<String>();
        for (Part part : parts) {
            System.out.println("content type: " + part.getContentType());

            //if the part has a content type then it is an audio file
            if (part.getContentType() != null) {
                Random generator = new Random();
                count++;
                System.out.println(part.getName() + " is an audio file and count is " + count);
                int name = generator.nextInt();
                songs.add("sound/uploadedfile" + name + ".wav");
                //so save to file
                try{
                part.write("uploadedfile" + name + ".wav");
                }catch(Exception e ){
                    e.printStackTrace();
                    e.getMessage();
                }
                //otherwise it is the select inputs content
            } else {
                //you use a scanner to get the content of this part
                programs = part.getInputStream();
                Scanner scanner = new Scanner(programs);
                if (scanner.hasNext()) {
                    program = "sound/" + scanner.nextLine();
                    //if for some reason a input stream isn't found you are redirected to an error page
                } else {

                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }

            }
        }
        //get the array list of file names
        //String[] songs = new String[count];
        //for (int i = 0; i < count; i++) {
        //songs[i] = "sound/uploadedfile" + (i + 1) + ".wav";
        //System.out.println("sound/uploadedfile" + (i + 1) + ".wav");
        //}

        try {
            //make list an array
            String[] songss = songs.toArray(new String[songs.size()]);
            //overlay program with songs
            overlay(program, songss);
            //output file to web service
            finish(request, response);
        } catch (Exception ex) {
            request.getRequestDispatcher("error.jsp").forward(request, response);
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        System.out.println("exiting doPost");
    }

    //@Override
    public String getServletInfo() {
        return "Serlvet that processes and overlays the sound files";
    }// </editor-fold>

    //not used, was messing up threading
    public void doUpload(Collection<Part> parts) throws IOException {
        System.out.println("entering doUpload");
        int count = 0;
        //create select's input stream
        InputStream programs;
        //String for the program location
        String program = null;
        //iterate through parts
        for (Part part : parts) {
            System.out.println("content type: " + part.getContentType());

            //if the part has a content type then it is an audio file
            if (part.getContentType() != null) {
                count++;
                System.out.println(part.getName() + " is an audio file and count is " + count);

                //so save to file
                part.write("uploadedfile" + count + ".wav");
                //otherwise it is the select inputs content
            } else {
                //you use a scanner to get the content of this part
                programs = part.getInputStream();
                Scanner scanner = new Scanner(programs);
                if (scanner.hasNext()) {
                    program = "sound/" + scanner.nextLine();
                    //if for some reason a input strea isn't found you are redirected to an error page
                } else {
                }

            }
        }
        System.out.println("exiting doUpload");
    }

    public AudioInputStream loadToWav(String c) throws Exception {
        System.out.println("in loadtowav loading " + c);
        //This was me trying to make things wait til they were uploaded before they were processed
//        File f = new File("C:/Users/Leigh/Documents/NetBeansProjects/RUNTHIS/src/java/"+c);
//        while(!f.exists() && !f.isDirectory()){
//           System.out.println("waiting for : "+f.getAbsolutePath());
//        }
        AudioInputStream ais = null;
        AudioInputStream rightais;
        String rightfile = "sound/file1.wav";
        try {
            //get the resource as an input stream
            InputStream is = this.getClass().getClassLoader()
                    .getResourceAsStream(c);

            //get the input stream of a file that is already in the format we want the output file to be
            InputStream rightis = this.getClass().getClassLoader()
                    .getResourceAsStream(rightfile);
            //fill audioinputstreams with these inputstreams
            try{
            ais = AudioSystem.getAudioInputStream(is);
            }catch(Exception e){
                e.getStackTrace();
            }
            rightais = AudioSystem.getAudioInputStream(rightis);

        } catch (IOException e) {
            //if this fails attempt to get the resource through the URL method
            e.printStackTrace();
            URL url = this.getClass().getResource(c);
            URL righturl = this.getClass().getResource(rightfile);
            ais = AudioSystem.getAudioInputStream(url);
            rightais = AudioSystem.getAudioInputStream(righturl);

        }

        //get the format from the audio clip with the desired format
        AudioFormat rightFormat = rightais.getFormat();
        AudioFormat sourceFormat = ais.getFormat();
        //you need a file with the correct format so that you can get the sample rate
        //just typing in the numbers you wantdoesn't work.
        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, rightFormat.getSampleRate(),
                16, 1, 2, rightFormat.getSampleRate(), false); // endianness

        //the ais that will contain the converted stream
        AudioInputStream targetStream = null;

        //test if conversion is supported
        if (!AudioSystem.isConversionSupported(targetFormat, sourceFormat)) {
            System.out.println("Direct conversion not possible.");
            System.out.println("Trying with intermediate PCM format.");
            //if not them try with an intermediate format
            AudioFormat intermediateFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sourceFormat.getSampleRate(), 16,
                    sourceFormat.getChannels(), 2 * sourceFormat.getChannels(), // frameSize
                    sourceFormat.getSampleRate(), false);
            if (AudioSystem.isConversionSupported(intermediateFormat,
                    sourceFormat)) {
                // intermediate conversion is supported
                ais = AudioSystem.getAudioInputStream(intermediateFormat, ais);
            }
        }

        if (sourceFormat == targetFormat) {
            System.out.println("They're the same format");
        }
        targetStream = AudioSystem.getAudioInputStream(targetFormat, ais);
        if (targetStream == null) {

            throw new Exception("conversion not supported");
        }

        System.out.println("target: " + targetFormat);
        System.out.println("source: " + sourceFormat);
        //return the stream in the right format
        System.out.println("endofloadtowav");
        return targetStream;
    }

    public AudioInputStream concatenate(String[] songs) throws Exception {
        System.out.println("In concatenate. Songs: " + Arrays.toString(songs));
        AudioInputStream concat = loadToWav("sound/noth.wav");
        System.out.println(concat.getFrameLength());
        File file = null; //changed for testing

        try {
            //for each song
            for (int i = 0; i < songs.length; i++) {
                System.out.println("concatenating: "+songs[i]);
                //load it to wav
                AudioInputStream second = loadToWav(songs[i]);
                //create a sequence input stream with the files concatenated so far and the new file
                SequenceInputStream sis = new SequenceInputStream(concat, second);
                System.out.println("i: " + i + " sound: " + songs[i]);
                System.out.println("concat: " + concat.getFrameLength() + " Second: " + second.getFrameLength());
                //figure out the length
                long length = concat.getFrameLength() + second.getFrameLength();
                System.out.println("length: " + length);
                //get the correct format
                AudioFormat fmt = concat.getFormat();
                //make a destination file
                file = new File("C:\\Users\\Leigh\\Documents\\NetBeansProjects\\RUNTHIS\\src\\java\\sound\\test" + i + ".wav"); //changed for testing
                file.getParentFile().mkdir();
                file.createNewFile();
                //fill an audioinputstream with the finished sequence
                AudioInputStream ais = new AudioInputStream(sis, fmt, length);
                //write it t the file
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);

                concat = AudioSystem.getAudioInputStream(file);

            }
            //int size = AudioSystem.write(concat, AudioFileFormat.Type.WAVE, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //AudioSystem.write(concat, AudioFileFormat.Type.WAVE, fileOut);
        //play(AudioSystem.getAudioInputStream(file));
        return concat;

    }

    //method used to play clips 
    //this was used during debugging for quick feedback on how the files sound
    public void play(AudioInputStream ais) throws LineUnavailableException, IOException, InterruptedException {
        Clip clip = AudioSystem.getClip();

        clip.open(ais);
        clip.start();

        do {
            Thread.sleep(15);
        } while (clip.isRunning());

        if (clip.isRunning() == true) {
            System.out.println("running");
        } else {
            System.out.println("not running");
        }

    }

    public void overlay(String p, String[] songs) throws Exception {
        System.out.println("in overlay");
        byte[] isbyte = null;
        byte[] isbytec = null;
        byte[] isbytenew = null;
        int[] data;
        int[] datac;
        int[] datanew;
        try {
            //get audioinputstream of the exercise program audio file
            AudioInputStream aisp;
            aisp = loadToWav(p);

            //get audioinputstream of the concatenated songs
            AudioInputStream aisc = concatenate(songs);

            //create byte array for 
            isbyte = new byte[(int) aisp.getFrameLength()
                    * aisp.getFormat().getFrameSize()];

            System.out.println(aisc.getFormat());
            isbytec = new byte[(int) aisc.getFrameLength()
                    * aisc.getFormat().getFrameSize()];

            // fill the byte array with the data of the AudioInputStream
            aisp.read(isbyte);
            aisc.read(isbytec);

            // Create an integer array of the right size
            data = new int[isbyte.length / 2];
            datac = new int[isbytec.length / 2];

            // fill the integer array by combining two bytes of the
            // byteArray to one integer
            for (int i = 0; i < data.length; ++i) {
                // First byte is MSB (high order)
                int MSB = (int) isbyte[2 * i];
                // First byte is MSB (high order)
                int LSB = (int) isbyte[2 * i + 1];
                data[i] = MSB << 8 | (255 & LSB);

            }

            for (int i = 0; i < datac.length; ++i) {
                // First byte is MSB (high order)
                int MSB = (int) isbytec[2 * i];
                // Second byte is LSB (low order)
                int LSB = (int) isbytec[2 * i + 1];
                datac[i] = MSB << 8 | (255 & LSB);

            }

            // Add 2 integer arrays together
            if (datac.length > data.length) {
                datanew = new int[datac.length];
            } else {
                datanew = new int[data.length];
            }
            int i;
            System.out.println("plussing " + (data[23] + datac[23]));
            System.out.println(data.length);
            System.out.println(datac.length);

            // if the music file is bigger than the audio cue file
            if (datac.length > data.length) {
                // add the 2 ints together
                for (i = 0; i < data.length; i++) {
                    datanew[i] = ((data[i] + datac[i]));

                }
                // iterate from where i left off add the remaining music file's
                // integers into the new array
                for (int j = i; j < datac.length; j++) {
                    datanew[j] = datac[j];

                }
                //else if the overlay is longer than the audio
            } else {
                /*for (i = 0; i < datac.length; i++) {
                 datanew[i] = ((datac[i] + data[i]));

                 }*/
                boolean goesdown = false;
                int keep = 1;
                int count = 0;
                for (i = 0; i < datac.length; i++) {
                    //if silence
                    if (data[i] > -1 || data[i] < 1) {
                        //turn volume down
                        //find a way of making this an integer
                        //int voldown = (int) Math.round(datac[i]*0.5);
                        datanew[i] = (datac[i] + data[i]);
                    } else {
                        datanew[i] = (datac[i] + data[i]);
                    }

                }
                for (int j = i; j < data.length; j++) {
                    datanew[j] = data[j];

                }

            }
            // convert the integer array to a byte array
            if (isbytec.length > isbyte.length) {
                isbytenew = new byte[isbytec.length];
            } else {
                isbytenew = new byte[isbyte.length];
            }

            for (int k = 0; k < datanew.length; ++k) {
                isbytenew[2 * k] = (byte) ((int) ((datanew[k] >> 8) & 255));
                isbytenew[2 * k + 1] = (byte) ((int) (datanew[k] & 255));
            }
            AudioInputStream done = new AudioInputStream(new ByteArrayInputStream(
                    isbytenew), aisc.getFormat(), aisc.getFrameLength());

            File finished = new File("C:\\Users\\Leigh\\Documents\\NetBeansProjects\\RUNTHIS\\src\\java\\sound\\finished.wav");
            finished.getParentFile().mkdir();
            finished.createNewFile();
            AudioSystem.write(done, AudioFileFormat.Type.WAVE, finished);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(p);
        }

    }

    public void finish(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
        //http://www.go4expert.com/articles/sending-files-browser-jsp-t3094/
        //PrintWriter out = response.getWriter();
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=finished.wav");
        //while (isprocessing = false) {

        java.io.FileInputStream fileInputStream = new java.io.FileInputStream("C:\\Users\\Leigh\\Documents\\NetBeansProjects\\RUNTHIS\\src\\java\\sound\\finished.wav");
        //set header to the finished file content length so i can see whether it has used the files i told it to.
        response.setHeader("Content-Length", String.valueOf(new File("C:\\Users\\Leigh\\Documents\\NetBeansProjects\\RUNTHIS\\src\\java\\sound\\finished.wav").length()));
        int i;
        while ((i = fileInputStream.read()) != -1) {
            out.write(i);
        }

        fileInputStream.close();
        out.flush();
        out.close();
        //}
    }
}
