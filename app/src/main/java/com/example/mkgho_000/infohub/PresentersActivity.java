package com.example.mkgho_000.infohub;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PresentersActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://private-ee02a-wsginfohub.apiary-mock.com/api/presenters";

    // JSON Node names
    private static final String TAG_PRESENTERS = "presenters";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_THUMB = "thumb";


    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenters);


        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        //image test code here
        //String endodedImage= "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxQSEhUUEhQVFRQUFhQUFRUUFBQUFBcYFBQWFxcXFxQYHCggGBwlHBQUITEhJSkrLi4uFx8zODMsNygtLiwBCgoKDg0OGxAQGiwkICQsLCwsLCwsLCwsLCwsLCwsLCwsLCwsLCwsLCwsLCwsLCwsLCwrLCssLCwsLCwsLCwsLP/AABEIAOEA4QMBIgACEQEDEQH/xAAcAAABBQEBAQAAAAAAAAAAAAAEAAEDBQYCBwj/xAA/EAABAwIEAwUFBgQEBwAAAAABAAIRAwQFEiExQVFhBiJxgZETMqGxwUJSctHh8AcUI/EVM2KSFiQ0gqKywv/EABsBAAIDAQEBAAAAAAAAAAAAAAIDAQQFAAYH/8QALREAAgIBBAAGAAUFAQAAAAAAAAECEQMEEiExBRMiMkFRM2FxsdEUI0OBoRX/2gAMAwEAAhEDEQA/ANfCdOoqjkTdCUrBL15JLdQ3iRueg5DqgCZMbAcFPdVuA3PFAXVcDujfj+qy9RktmzpMNKxV6vAadU1CiTw8zoFzTEHXU/dH1Kl9qB7xH4Rqq1l6g6g4DYz4fmdPmpwyfsnXrr+arDizW9Pn6DVTU8TcRLabz5Bo+K60RtkFvsRElo89fmqyq0MdoT6D6Livi9adKXmVDQp1Khl2nSUVkRg0+TRUKrH0wHQeukjRQMsg0yNunL6qxwrD+6Wu2MROsHofJO6iWmD+h8F0o/JylVpFTe2mUFzDIO7f04FUdQNdyPQ6EeB/utg+nI+c7FZLGrESSJafMg+f0MoF2HdoktCA4OAh2zmnZ7fDYkc+K0dlUBkAyBEdARsvPDcObpp5zr+R9VaYVjZBAJyk8HbHz4K5p8rx8PoztVg8xWuzcwllQ1heB4jZwiWncdeo6o0BakWpK0Y8k1wyLKnyqTKmhSCcZUsq7AXWVSSQkJsqlITQuOIyFzClITZVFHHEJiF3CQaookjhJSZU66jiN71X3lfKCT+yiripl1PJZjE70GdVTzzpF3TYtzFc3pmG++7UngBzlcNqhjZkAcXnj+EKptqkyZzSdeDdNhqiqj2yMxlw2aNx+XwWfKJswa6CBcud7oyjmePUrplqTq4yOQho9UPXuobOjQduLz+EfVc2tV7vdEdXST4lQosPckXVpQaNhH4Wyf8AcUe6o2NyT1eCf9oQFpg73auJPmY9Fo8Nwdrdwu8sF5CkNk6odAQPE/JaLCcGDYkeoVvZ2IHBWlKjCfjwN8srZdTSpAQtYGiEu7eRMK7yKKvRkJ08XHBWhmp8mMvahp6jzH1BWdv7lrgfiDt6LW4xS0Mrz/GO44ngs7a0zVg1KNlReNAOmrfH6HdRUXCfo7Yjkpajw7fbcOHDxQtamW7FPiInE0llclga5rjkHuuJl1In7LvvMK2mE3oqszbOGjhyI+i8ysrgnTmII4GfotJ2NviKppnZwMeLdvhKuYMu2VGdqMFxbXwbUpkk8LQMsYJylCcBcSRkJQpsqYtXURZEWpoUpCaFxNkeVKFIVyQuOOYTLpJcSVGO91hdya75aLzy6rOdx01J/fovT8RoNNN2eMoEmdo4rze7ojMOIBkT8J8ln6hVJGno3cWQNqFgDG6vPwlFUi2k3Me8evEn6IFlQMzOduZ8deA6mIU+G0nV3aiG8ANgFWqy8pUGYVZuqvzPkk/DoFtsOwsCNEFhFjkIWqtgESiLeQktbYAbI6lTAUbCpmJijQtysKo6InMg6ZRDU2LorzXJJmXLimlcuKlsFIqMXohwPBec45Zuk7r1O4ph26zeNYQMrnD0VLNjfuL2my16WeR1SGuInfdu3oefgVIHxAOoPunbyKNxUAOLXtBE6SI+WyAzho4mmY/Eydp5jqgXJZmq7HdTgy3fi1aPs1lc9pOhBP8A6/oFnqQ+WjgR6EFXGC14dBMH97I4ypiMsbi6PQ6DpHgpoVdhdfM3q0wQrJa8JblZhTW1jJBPCaEYux5TEpQlC445SXRCYhcSMlCSdcQNCSdJcSZ7tfckNbTaD3u849BsI8fksPXMS523LitVi1Se873jOnCJgBY3GJ1nb4LJyz3TNzTQ24ylvLuXgnYyeg1P5r0nsXZtdSDhrrE+C8nvz3vQL1H+F96G0HZtgQR8R9Ee3hAObtmxp20I6jTQf+L0uMjxCanitMnRwXKkRyW9NqmbogGXQ4FTfzAKMgPplEtcs5eYn7NsjXkFQ1Mauah0Ipt5nRDvpneW2b99drdyhn4lT2zLF/41aM/6m9Zm4j2jeG+g1R2H45htwclKtTc4jbNB9DqitvpAbUn2akOB1XFakHNIPFQWdAMENMtRjtlDVo5d8HlvanCMriQNFj6zC0mBO/6ghex45aZ4HPj1WCxfA303QdZOnGZ2VL2s1Iy8xUzJsAIIboRrHLwPL5IrDX66/wBuajxegaFYsIhzYd5fsFT2NMGo0t2cduRO4Tasrz4Nhg185k5hvE+m61FOpICoRbZafVhyu6h39x6K1wx8tHTRXMO6EtrMvPtmty7DwnhME6ulOhEJiUiUoXHUMkugkQoOOUinhPCk44SXUJlxxjMbMPJ4CAB5fmVk8XbJA/eq02OOl0cBp4xCorun3iTwHzhYuT3tnoNP7EYzFGwV6F/Dei2pRImMmWY8CVhsWZqtT/B66/5itSOz6WYD8DgD8HKxHlIRk9MmaC/7W21J/sWUnVnnQNZBJKpsS7SUw8tdY1abgYcRUhwmOAHVbGl2MpsqOqNGrtRuCN9j5lGnBGOIdUosqO5vDXeE6I6j8i/X8MymFY1DQZeGu1b7Qa7xoeI0WvwVxrDQoirblzSzIwN2gNEDyRHZu3FNxAGnpsh22+A2+CO+wwtEjUrN3mDmtUAqsd7IEkjYOjhE668f1XpdVigqUQQuWOmR5lqjyT/gAiq51J3s2HMJktcGv3aQDDhw31C0dr2FtXezzsDyxoY2NAMuxkbukyT1WvNoOiJt6EJnIFRXJBZ2TaTQ1smOck/FdPKmqmEK96GTDgvkgqCSPFPb2UOe5xnMe6PugCAAuA7WTsNVW3/a60ptzNqCo7gxhzEny28SqzSu2Hb6R5p/Fl4bfsDfs0mh0c3OcflHqqXCrjKdDt3hpyUXaKtUrV31au9R09ANgB0AACisSQ8Hwn0hE+gkn8m7w/tAKjHh+hOXb/Tv8lqMBB9mCRE6x47LzjB2j27WEfaGnxXq9BoAEKzp7k7fwUNVUFS+SaFzC6SKv0Z5zCZdwmyrqRNsQCeEoTErtpFsdNKRXEqKJs7TrjMmXUTZk8ctMrQehg+crNXbO7PFztfBup+fwWuxV4czzWYvm6EcgB/u1Pw+ax9QkpcG3pG9pkMUb+/KUf8AwvrZcRZ/qZVb493N/wDCExDXN+I+gCrcHvv5e6o1uFN7XH8Ozv8AxLk3F0dm7s+lqDpU3swqehdjQg6HUHmDxRbr4NCKznAIu2iN4TYXTEyFjrzGzWuGUgctMvAe76ecR5rbWFVjRlEaI4OxckWj0Hc3rWCXA5eJjQeKnqVxG6Dr3THAsEOOxG4HiplL8wIx+0dU7pjtWkHzU7asLBX2GVbY56BMcWH3T4ckThnaYO7r+64bg/TmleYO8tPpmrrP1UDnoeneBw0UoCFuw2lFAmLuyW9V3Km8+jSvGcGoz4dV7fd0PaU3M+81w9WkLx7DsrSWAw5sgg7hKy8HaZ3ZBi1ty26ahVVm8Z4mNeKtL+3eHaAg9Pdd+RVTUpFx1EHmugw8nBc4LP8ANsjWXhevsaAvKezNw2gQ4tLnjYu0A8Ft7THMxGYQFe084xVNmXqYzk7o0SaELTus2wJCnbU56K4mn0UmmjqU8pimlSQdErmEiuZXHHRCjcu1y4LjhkkoSXHGPDp0O3FVOI6eZn12+ACugyN9t1QY5V4ysTI7Zu6eLSsy7xMjnnj0VBdCD4b+a0dmyXHwMKivW/1HJmN80TmVxR6T/D/GTWtfZky+3hh5lkdw+gLf+1Wt/iTspA8F5V2dxY2ldtUatIy1G/ead/MbjwXr1vTp1mZqZDmvGZpHEFFNVyDjnxTKazphxV1RZVb/AJbiehMqmv8As6HmQXNPBwcQR0kKPDKL6JLbmo/KHANrAEw1wM5zGkEDvbQVHYyEPk29q6s9sP8Agre1NOmNSB8VlbO2Y5ub+cBbMHvNOxgxqryiLemQBNUkdH6zy28xyRRh9kTcOlb/ANE99iDS05WucBHeiG6mJk8FjK1tVuGOq+xFNuuWXS8gGA6ANJ3WybZPrBpuMoAAHsWe44hwcHukSSC0QJgddCj61uMsRopnChadGY7OWzsgdJ5EK+zIagzJn6u0TtknTcpSIySss7VkyfJUWIdlqNV2ZzBmn3hofULSUGw0BOWarprcLhJxfBj3dlSBDXacnaoet2SduGUyfMLc5F0GpccaGyzyo8jxbD6tIxUpNYOB1c0+ahoV3t4iOUL1+6tGVGljwHNO4K837TYC61dIl1Jx7p4tPI/mtPT4sT4fZ5zxPJq8X9zG7X1XR1YYoNAXFvyV9bPzDR8+iw/VEWl29h7pIPw9Fc/pkujJxeNS/wAqv9DcZyN1K0Khs8eB0qaHnwKuKF00jQoXFxfJsYdTjzK4MIhMQo/bBdNcFHA46STFyeVxw6S4lJTZxicQuC0LN4s4keUq9uGh5B8VVYpSk+Py4LAlwz0WDlFdhNCXgcT+ys5iNOKrvE/Na7CGxVnbKD8o+qzuL0++T1lHjl6ici9JT1WRornsv2nqWbgDLqJMlnEdW/lxVZWGpUDmyOoVntFOSp8HumE3LK7RUpkOY8Zh48R0I5IqsCBoAehXkHYPtKbOsA8/0HkB4OzDwqD69PAL3JlJrwCNjrpqh2UFDK07RQ0mUp1t9eJAHzVrh4cD3KYZ1Jkq0t7JvEI2jQbwClJ2WJaqUlyNbMPHUolzdE5ICEv7zI3qdgpf5lNu2BX9QAp8PZxO5QLKDnd5ysLbTRKOLBhUgKgBXbXIqBsnTZlGXqN70tdhBbXKl7aVWNsq7n7BhI/F9mPOFMbwArBfxYxv+g2i0++6T4N/WFf0sd+RRKuo9MGygwa5Fai08SO94jQ/JEAR6wqjs1RLKYB31Mcs2qtaj9VsVXDPAZ4KOWSj1ZMHIi3rObsUJTPNSNcppMSnKLuLouKOJE+KIp4kqGVJTdPiqebTteqB6Dw/xXc1jzf6f8l//iS7GIrPklN7QqnvZ6LYjR/4kElnfaFJTvZGwalb68hoIVXiW5Kv2VgdB1VRiNLj5rHycSNzTW48lRamJPNV+JU5BPMlWjGcQgcRcNhw+pUxGS6MtWOqRGqkvKe6ipOn4q4uihLsgczUrddiO3RtmChcE+zboypvkH3XDfLyPDwCxhbKarS0R2BVHv8AY9oqb2hzXtc0jQhwI+CMt8dZPvD1Xzzg9+aLoPunfp1WytrnYzIKho5SPV7rtNTboDmdwA1UNrWNU53enALFW1dpC0GGYgBohas5s1YAhQOMFBHEOSjbdSVFEFwyqpPawq+lWU4fIXAhrnSNELVqwuWuIPRPdNGWQhUbJuijxO5I1C87xprri8a0yW02hzvWY+S3l+JlY+m8AOqcapkfhGjR6CfNbHhOG8259JcmT4xqfK09LtukQ03w8+ITVz3kNmBJmQeaj9vGjueh/NWsuXfNy+zyix82WbHKQFQNdA1A8U4fqiTEOIZK7YFBm2XYKIS0FTKdtOVCHRxRLHws7VYlGW5dM9X4NrHlx+XN8r9hvYJ117UJKraNsa2ALnEbCR5oK9bmGnEwjKX2/OB1On1Qd8/2bT94/BZWVet0aulk/JTZUX9UNEDw9FR3FXijsRJ06D5lABsgnyHmjiqGSk30A5ZOu06qG4pNFT+nJbOmbQ7Kz/l9EK6l3gnRmJljIaVKWnpKgqFWVIQHfvgq6oO8UUXbFyjQK9mqvsDuDlg8NFU0GZnho4lau3wbKJCNySFKN9BFrWjjormzd1VC+3IXdJ5GxhSnYLVG+tqoLFE2vBWWt8Re3irCnflyhog09vdq0oPlZChUMrTYW+QhYSQdUJhR06hIgot0RqhqlZoRxVcgy5M52sreyoOP2nxTb4vMfASfJYa7qzAGzYHor3t1iHtKlJgOjMzz4xlHzKylatElbOCXl6Z/cn/xHmvFXvzqK+F+5PTeDpyXFPvOjhqhbWp3Z56qe1fALkpMzpQqwtlTJAJ7pMeH6Kw2ghVLdWifFGtqECM2kaDimxlRWyQsMJC7FSdtAhGu47lSg6fRMTK7jRMG7IiieCFDtApqJ9Nl0oqSpk4cssORZI/DC/ZpLj2xSWf/AEkj0v8A6+IKqsyF55az48Pgs9eVC7zK0mPGGmOOpWZOuUhY+T8Rs9XgX9lRK/E6fyHkubehDVYX1LMD1ULBqR4Id3BbS5BnUtQOQQNdkVAOhKuWsAJ8AqwxmqPP4QpiyJqqGq2v9ORuqStT1WjzEU2yN1WXdGH6bFMhLkr5lSsq6bC0gjQgyFrcL7QNIDX6HmqCpS0QTRrld6q48aaMHFq5b3Z6S3I8SCE9CyadFjLCtUpbEkLS2OJE+KQ40akcikEXWG5fdUdqCDBVi26zDVDVaeshcmw+CxtXc1aWuIZdFn6NUp6pPBSczR1cY6qixntI1oOqp7rPzVLcWZce9qEzHFzkor5E5JqEXJ/BI+5L5e73nbdBwVbidaGxzR9TeOSo8SqS6FpZmo+ldLg8tjbzZnN/PJYUXf0/gu3VYysHiVFR90eqjtn5qjieAgILBcbbZbB2m6mougdZ4qGhTGXMT4BSscSmIpSDBU0hdmp0QntNVIKhRpldwCQ4qenVQgJ4qRpRpipRD/apIPOkisV5ZeYu7MD4T8VnrBvejlKublxB12MifigqTYJcNhuvMZVy2fVMD6Q5pg7qnDv6xHl5QruvoCeazt4cjy7wjzScaL0+I2Wt9bf0vaN1jQxy/usuTmLW8CZK0+C3weHMOzmmR9Qs7WoFlUtPAwEyHDaFze5Jh1Vwe4NH2Qq7FXZXhHYPTl73Hw+Kr+0bYqenxRw91Cc34dkTSCUFe09VJZvXd4JC0or0nkp+nMywwKuKgyu3CvKVtCw9rcGm8Eefgt3htyHsBCXkhatF/Bm2y2S6fX8BNNSMeo3GEM+vBVejSUi2pwpCFXULlTm5RBWNcgKnvakGOSPubiAfgqGs+TJV/SR2J5H+i/kxvFtRwsK+eWcPOh57rP3R75VoKhJJVTW98qZOyhpo7WyxfVhnko8HBMnmfko7t3djoiMJ0YFC7JkqxN/bLYu0gaqShVgFVNxXLT0hFWD+5qPNMUuSjLFUbDWv1U7Sgqb5U7SjTESiEt0RDChKZ/upW1PijTESiEZ0lHokisXSLRryUSaIiBsdXKPD6RJkcPRLFahDe7x3PNefzcyo+jYXtV/QLXGkcDsqvGreWNPEaH6IrFHENZG4EqL+ZFWlro4fFV9rXJoRyqaaZX4UMtVhPUKftBTy1Gn73d/JRUqocJPvN+MLjtNd5mU+ep9EXckRe2DGwjuuIPX5qrxqpme4nw9FZWjw4B447+Kgx60Aio3Z3vDkeaPH7hWb8Pgz9k7vEFGuPBVrjD55qxWlDo8vqV6rA6rNUVheJOou01bySrU0I9qIiMlJUzYU8bpvGstKGva+kt1HRZ2gZ6FENe5p00QPHFjlqskXT5LS1xHqrOncyJWbbcx9kA84UjrpzhqVHlL7Dl4hJLiJZ319J0OiGdUkSgASUU8d1WU+KMnJcpbpdsHtjMqur7nxVhabIKsyXOQMsYnU2Ndu7o8kZhhhsdSUHVbLPBSWNTRQuw5xvHS+zq5fmqAdVZF0NAVRZ61HTwRr6hcdOGilMTlh1H6D6VTRTUnaoKgCimFMiynOKDmPXTNUKx3JEU0yytKNEspJkyKxVGoqaAMbx3PMgp2tzHo0ZfE/3KjouEb7blE0iAJ+yNfNYblwfQIxt0UWPUhOm4EAKiNJzRy5BXmIN9o4HcnYcupQzqZPdGpS+YrkdGpypfBn9cx9UPilQnL0Cubu2ygjidyqC63RY/Uws8lGPIRhlchpB2KLq3OdvXiOfVVoq7AbKOpWyeCOWN3ZXx6lPhg95SRFE91Q1ak7bKS25K1iuuTL1sUuginqFBWYu5gqSo2RKdRnJ0wAggyFY21QPHVCObKjaS0yPRd0NnHevzLF1GF2KYStq4eIT1WQipFRt3T7OANVJU28lEzdTO1Ckh9g9q3VQVWd8hTWx7ye7ZD55qK4Gp1MEDOCHpHUt47hHu3Vfd91wcEDLGJ7uDug+A4nefop7WYlAXVSRI47qxoOGUcdFyCyqo39h1MGJUzHoRj58ERSgJi/Iz5oMZ0RFOQh6T9FKx2vimplSQVmTJpSRCKLql7vmrKr/lfvkkksLJ7D6Fj95X0/ohLPdySSjP2v0D0vT/VldiXFZ6rskkj0/QjX9g1NR3uySStGZH5BqGyKpbpJI4i9QSuXbdkkk1FF9EK4CZJQOid2XvK1q7Jkl0eivn94Md1MzZJJGBIGo+95qW+94JklHwE/egeogrxJJAy1g9wL9kqzo7DwSSUD83tC6KK4JkkyJl5OyZiIbwTpJqKswlJJJGVz/9k=";
        //byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
       // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String title = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                String company = ((TextView) view.findViewById(R.id.company))
                        .getText().toString();
                String thumb = ((TextView) view.findViewById(R.id.thumb))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SinglePresenterActivity.class);
                in.putExtra(TAG_NAME, name);
                in.putExtra(TAG_TITLE, title);
                in.putExtra(TAG_COMPANY, company);
                in.putExtra(TAG_THUMB, thumb);
                startActivity(in);

            }
        });

        // Calling async task to get json
        new GetContacts().execute();

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PresentersActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_PRESENTERS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String title = c.getString(TAG_TITLE);
                        String company = c.getString(TAG_COMPANY);
                        //String thumb = c.getString(TAG_THUMB);


                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject(TAG_COMPANY);
                        //String mobile = phone.getString(TAG_TITLE);


                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_TITLE, title);
                        contact.put(TAG_COMPANY, company);
                        //contact.put(TAG_THUMB, thumb);


                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    PresentersActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_TITLE,
                    TAG_COMPANY }, new int[] { R.id.name,
                    R.id.title, R.id.company });

            setListAdapter(adapter);
        }

    }

}