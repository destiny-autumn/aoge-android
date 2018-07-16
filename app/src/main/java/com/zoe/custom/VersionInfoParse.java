package com.zoe.custom;

import android.util.Xml;

import com.zoe.entity.VersionEntity;

import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;

/**
 * Created by scorp on 2016/12/29.
 */
public class VersionInfoParse {

    public static VersionEntity getUpdataInfo(InputStream is) throws Exception{

        XmlPullParser parser = Xml.newPullParser();

        parser.setInput(is, "utf-8");

        int type = parser.getEventType();

        VersionEntity info = new VersionEntity();

        while(type != XmlPullParser.END_DOCUMENT ){

            switch (type) {

                case XmlPullParser.START_TAG:

                    if("version".equals(parser.getName())){

                        info.version=parser.nextText();

                    }else if ("url".equals(parser.getName())){

                        info.url=parser.nextText();

                    }else if ("description".equals(parser.getName())){

                        info.description=parser.nextText();

                    }

                    break;

            }

            type = parser.next();

        }

        return info;

    }

}
